package com.example.springskillaryback;

import com.example.springskillaryback.domain.CategoryEnum;
import com.example.springskillaryback.domain.Content;
import com.example.springskillaryback.domain.Creator;
import com.example.springskillaryback.domain.Post;
import com.example.springskillaryback.domain.SubscriptionPlan;
import com.example.springskillaryback.domain.User;
import com.example.springskillaryback.repository.ContentRepository;
import com.example.springskillaryback.repository.CreatorRepository;
import com.example.springskillaryback.repository.PostRepository;
import com.example.springskillaryback.repository.SubscriptionPlanRepository;
import com.example.springskillaryback.repository.UserRepository;
import com.example.springskillaryback.service.SubscriptionService;
import org.springframework.boot.CommandLineRunner;
import com.example.springskillaryback.domain.*;
import com.example.springskillaryback.repository.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringSkillaryBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSkillaryBackApplication.class, args);
	}

	@Bean
	@Profile("dev")
	public CommandLineRunner cml(
			SubscriptionPlanRepository subscriptionPlanRepository,
			CreatorRepository creatorRepository,
			UserRepository userRepository,
			ContentRepository contentRepository,
			PostRepository postRepository,
			PasswordEncoder passwordEncoder,
			OrderRepository orderRepository,
			PaymentRepository paymentRepository,
			SubscriptionService subscriptionService,
			RoleRepository roleRepository
	) {
		return args -> {
			// 1. 역할(Role) 조회
			var roleCreator = roleRepository.findByRole(RoleEnum.ROLE_CREATOR);
			var roleAdmin = roleRepository.findByRole(RoleEnum.ROLE_ADMIN);

			// 2. 유저 생성 및 역할 부여 (save는 한 번만)
			var admin = User.builder().email("admin@admin.com").password(passwordEncoder.encode("123456abc!")).nickname("admin").build();
			admin.getRoles().add(roleAdmin);
			userRepository.save(admin);

			var user2 = userRepository.save(User.builder().email("email@email.com").password(passwordEncoder.encode("123456abc!")).nickname("hello").build());

			var user = User.builder().email("email2@email.com").password(passwordEncoder.encode("123456abc!")).nickname("hello3").build();
			user.getRoles().add(roleCreator);
			userRepository.save(user);

			var user1 = User.builder().email("email1@email.com").password(passwordEncoder.encode("123456abc!")).nickname("hello2").build();
			user1.getRoles().add(roleCreator);
			userRepository.save(user1);

			// 3. 크리에이터 생성
			var creator = creatorRepository.save(Creator.builder().displayName("테스트 이름").user(user).category(CategoryEnum.IT).build());
			var creator1 = creatorRepository.save(Creator.builder().displayName("크리에이터2").user(user1).category(CategoryEnum.ART).build());

			// 4. 구독 플랜 및 구독 처리
			var subscriptionPlan = subscriptionPlanRepository.save(SubscriptionPlan.builder().name("테스트플랜").price(100).description("test").creator(creator).build());
			subscriptionService.subscribe(user2, subscriptionPlan);

			// 5. 콘텐츠 및 포스트 생성 (연관관계를 맺은 후 한 번만 save)
			// [유료 테스트]
			Content content = Content.builder().title("[유료] 테스트").description("콘텐츠소개").price(1000).creator(creator).category(CategoryEnum.IT).build();
			Post post = Post.builder().body("본문 입니다.").creator(creator).content(content).build();
			content.setPost(post); // 관계 설정
			contentRepository.save(content); // Content와 연관된 Post가 Cascade 설정되어 있다면 한 번에 저장됨

			// 결제 데이터 생성
			var order = orderRepository.save(new Order(1000, user2, content));
			paymentRepository.save(new Payment("toss_pk_test_sample_key_20260118", 1000, order, CreditMethodEnum.CARD, user2));

			// [구독 테스트]
			Content content1 = Content.builder().title("[구독] 테스트").description("콘텐츠소개").plan(subscriptionPlan).creator(creator).category(CategoryEnum.IT).build();
			content1.setPost(Post.builder().body("본문 입니다.").creator(creator).content(content1).build());
			contentRepository.save(content1);

			// [무료 테스트]
			Content content2 = Content.builder().title("[무료] 테스트").description("콘텐츠소개").creator(creator1).category(CategoryEnum.IT).build();
			content2.setPost(Post.builder().body("본문 입니다.").creator(creator1).content(content2).build());
			contentRepository.save(content2);
		};
//		return args -> {
//			var adminRole = roleRepository.findById((byte) 2)
//					.orElseThrow(() -> new IllegalArgumentException("admin 없음"));
//			var admin = userRepository.save(User.builder()
//			                                    .email("admin@admin.com")
//			                                    .password(passwordEncoder.encode("123456abc!"))
//			                                    .nickname("admin")
//			                                    .build());
//			var user2 = userRepository.save(User.builder()
//			                                   .email("email@email.com")
//			                                   .password(passwordEncoder.encode("123456abc!"))
//			                                   .nickname("hello")
//			                                   .build());
//			var user = userRepository.save(User.builder()
//			                                   .email("email2@email.com")
//			                                   .password(passwordEncoder.encode("123456abc!"))
//			                                   .nickname("hello3")
//			                                   .build());
//
//            var user1 = userRepository.save(User.builder()
//                                                .email("email1@email.com")
//                                                .password(passwordEncoder.encode("123456abc!"))
//                                                .nickname("hello2")
//                                                .build());
//
//            var roleCreator = roleRepository.findByRole(RoleEnum.ROLE_CREATOR);
//            var roleAdmin = roleRepository.findByRole(RoleEnum.ROLE_ADMIN);
//            admin.getRoles().add(roleAdmin);
//            user.getRoles().add(roleCreator);
//            user1.getRoles().add(roleCreator);
//            userRepository.saveAndFlush(admin);
//            userRepository.saveAndFlush(user);
//            userRepository.saveAndFlush(user1);
//
//
//            var creator = creatorRepository.save(Creator.builder()
//			                                            .displayName("테스트 이름")
//			                                            .user(user)
//                    .category(CategoryEnum.IT)
//			                                            .build());
//
//            var creator1 = creatorRepository.save(Creator.builder()
//                                                        .displayName("크리에이터2")
//                                                        .user(user1)
//                    .category(CategoryEnum.ART)
//                                                        .build());
//
//
//			var subscriptionPlan = subscriptionPlanRepository.save(SubscriptionPlan.builder()
//			                                                                       .name("테스트플랜")
//			                                                                       .price(100)
//			                                                                       .description("test")
//			                                                                       .creator(creator)
//			                                                                       .build());
//			subscriptionService.subscribe(user2, subscriptionPlan);
//
//			var content = contentRepository.save(Content.builder()
//			                                            .title("[유료] 테스트")
//                                                        .description("콘텐츠소개")
//			                                            .price(1000)
//			                                            .creator(creator)
//			                                            .description("test")
//			                                            .category(CategoryEnum.IT)
//			                                            .build());
//
//			var order = new Order(1000, user2, content);
//			order = orderRepository.save(order);
//
//			var payment = new Payment(
//					"toss_pk_test_sample_key_20260118", 1000, order, CreditMethodEnum.CARD, user2
//			);
//			paymentRepository.save(payment);
//
//            var post = postRepository.save(Post.builder()
//                                                .body("본문 입니다.")
//                                                .creator(creator)
//                                                .content(content)
//                                                .build());
//            content.setPost(post);
//            contentRepository.save(content);
//
//            var content1 = contentRepository.save(Content.builder()
//                                                        .title("[구독] 테스트")
//                                                        .description("콘텐츠소개")
//                                                        .plan(subscriptionPlan)
//                                                        .creator(creator)
//                                                        .category(CategoryEnum.IT)
//                                                        .build());
//
//            var post1 = postRepository.save(Post.builder()
//                                                .body("본문 입니다.")
//                                                .creator(creator)
//                                                .content(content1)
//                                                .build());
//            content1.setPost(post1);
//            contentRepository.save(content1);
//
//
//            var content2 = contentRepository.save(Content.builder()
//                                                        .title("[무료] 테스트")
//                                                        .description("콘텐츠소개")
//                                                        .creator(creator1)
//                                                        .category(CategoryEnum.IT)
//                                                        .build());
//
//            var post2 = postRepository.save(Post.builder()
//                                                .body("본문 입니다.")
//                                                .creator(creator1)
//                                                .content(content2)
//                                                .build());
//            content2.setPost(post2);
//            contentRepository.save(content2);
//		};
	}
}
