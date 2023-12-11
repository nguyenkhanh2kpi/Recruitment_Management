package com.java08.quanlituyendung.initData;

import com.java08.quanlituyendung.entity.*;
import com.java08.quanlituyendung.repository.*;
import com.java08.quanlituyendung.service.impl.QuestionServiceIml;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class DataInitializer implements CommandLineRunner {
    private final UserAccountRepository userAccountRepository;
    private final UserInfoRepository userInfoRepository;
    private final CompanyRepository companyRepository;
    private final JobPostingRepository jobPostingRepository;
    private final EventRepository eventRepository;
    private final InterviewRepository interviewRepository;
    private final CvRepository cvRepository;
    private final SkillRepository skillRepository;
    private final PositionRepository positionRepository;
    private final QuestionRepository questionRepository;
    private final QuestionServiceIml questionServiceIml;
    private final BlackListRepository blackListRepository;
    private final InterviewDetailRepository interviewDetailRepository;

    public DataInitializer(UserAccountRepository userAccountRepository, UserInfoRepository userInfoRepository, CompanyRepository companyRepository, JobPostingRepository jobPostingRepository, EventRepository eventRepository, InterviewRepository interviewRepository, CvRepository cvRepository, SkillRepository skillRepository, PositionRepository positionRepository, QuestionRepository questionRepository, QuestionServiceIml questionServiceIml, BlackListRepository blackListRepository, InterviewDetailRepository interviewDetailRepository) {
        this.userAccountRepository = userAccountRepository;
        this.userInfoRepository = userInfoRepository;
        this.companyRepository = companyRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.eventRepository = eventRepository;
        this.interviewRepository = interviewRepository;
        this.cvRepository = cvRepository;
        this.skillRepository = skillRepository;
        this.positionRepository = positionRepository;
        this.questionRepository = questionRepository;
        this.questionServiceIml = questionServiceIml;
        this.blackListRepository = blackListRepository;
        this.interviewDetailRepository = interviewDetailRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userAccountRepository.existsByEmail("admin@gmail.com")) {
            GenerateUser();
            GenerateSkillPosition();
            GenerateQuestions();
        }
    }

    public void GenerateUser() {
        var admin = UserAccountEntity.builder()
                .authenticationProvider(AuthenticationProvider.LOCAL)
                .creationTime(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .email("admin@gmail.com")
                .password("$2a$10$zuZ.0a0OYWNA9nqc6mu5CuuySyvvrIf7CjnWot1Bez.QQowsg.Nhi")
                .role(Role.ADMIN)
                .status(Status.INPROCESS)
                .state(UserAccountEntity.State.ACTIVE)
                .username("admin")
                .build();
        var reccer1 = UserAccountEntity.builder()
                .authenticationProvider(AuthenticationProvider.LOCAL)
                .creationTime(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .email("reccer1@gmail.com")
                .password("$2a$10$zuZ.0a0OYWNA9nqc6mu5CuuySyvvrIf7CjnWot1Bez.QQowsg.Nhi")
                .role(Role.RECRUITER)
                .status(Status.INPROCESS)
                .state(UserAccountEntity.State.ACTIVE)
                .username("reccer1")
                .build();
        var reccer2 = UserAccountEntity.builder()
                .authenticationProvider(AuthenticationProvider.LOCAL)
                .creationTime(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .email("reccer2@gmail.com")
                .password("$2a$10$zuZ.0a0OYWNA9nqc6mu5CuuySyvvrIf7CjnWot1Bez.QQowsg.Nhi")
                .role(Role.RECRUITER)
                .status(Status.INPROCESS)
                .state(UserAccountEntity.State.ACTIVE)
                .username("recccer2")
                .build();
        var interviewer1 = UserAccountEntity.builder()
                .authenticationProvider(AuthenticationProvider.LOCAL)
                .creationTime(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .email("interviewer1@gmail.com")
                .password("$2a$10$zuZ.0a0OYWNA9nqc6mu5CuuySyvvrIf7CjnWot1Bez.QQowsg.Nhi")
                .role(Role.INTERVIEWER)
                .status(Status.INPROCESS)
                .state(UserAccountEntity.State.ACTIVE)
                .username("interviewer1")
                .reccerId(2L)
                .build();
        var interviewer2 = UserAccountEntity.builder()
                .authenticationProvider(AuthenticationProvider.LOCAL)
                .creationTime(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .email("interviewer2@gmail.com")
                .password("$2a$10$zuZ.0a0OYWNA9nqc6mu5CuuySyvvrIf7CjnWot1Bez.QQowsg.Nhi")
                .role(Role.INTERVIEWER)
                .status(Status.INPROCESS)
                .state(UserAccountEntity.State.ACTIVE)
                .username("interviewer2")
                .reccerId(3L)
                .build();
        var candidate = UserAccountEntity.builder()
                .authenticationProvider(AuthenticationProvider.LOCAL)
                .creationTime(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .email("candidate@gmail.com")
                .password("$2a$10$zuZ.0a0OYWNA9nqc6mu5CuuySyvvrIf7CjnWot1Bez.QQowsg.Nhi")
                .role(Role.CANDIDATE)
                .status(Status.INPROCESS)
                .state(UserAccountEntity.State.ACTIVE)
                .username("candidate")
                .build();
        var backlistUser = UserAccountEntity.builder()
                .authenticationProvider(AuthenticationProvider.LOCAL)
                .creationTime(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .email("backlist@gmail.com")
                .password("$2a$10$zuZ.0a0OYWNA9nqc6mu5CuuySyvvrIf7CjnWot1Bez.QQowsg.Nhi")
                .role(Role.CANDIDATE)
                .status(Status.BLACKLIST)
                .state(UserAccountEntity.State.ACTIVE)
                .username("candidate")
                .build();
        var adminInfo = UserInfoEntity.builder()
                .userAccountInfo(admin)
                .avatar("https://firebasestorage.googleapis.com/v0/b/upload2-23381.appspot.com/o/files%2Fhinh1.jpg?alt=media&token=c16bc73f-a3be-4c01-9f26-5a0d4ccad233")
                .address("416 Lã Xuân Oai, Long Trường, quận 9, TP.HCM")
                .fullName("Nguyễn Admin")
                .gender(Gender.MALE)
                .phone("012334488")
                .build();
        var reccer1Info = UserInfoEntity.builder()
                .userAccountInfo(reccer1)
                .avatar("https://firebasestorage.googleapis.com/v0/b/upload2-23381.appspot.com/o/files%2Fhinh1.jpg?alt=media&token=c16bc73f-a3be-4c01-9f26-5a0d4ccad233")
                .address("59 Đường 379 Tăng Nhơn Phú A, quận 9, TP.HCM")
                .fullName("Nguyễn Reccer1")
                .gender(Gender.MALE)
                .phone("012365488")
                .build();
        var reccer2Info = UserInfoEntity.builder()
                .userAccountInfo(reccer2)
                .avatar("https://firebasestorage.googleapis.com/v0/b/upload2-23381.appspot.com/o/files%2Fhinh1.jpg?alt=media&token=c16bc73f-a3be-4c01-9f26-5a0d4ccad233")
                .address("20 Đường 379 Tăng Nhơn Phú A, quận 9, TP.HCM")
                .fullName("Nguyễn Reccer1")
                .gender(Gender.MALE)
                .phone("012334238")
                .build();
        var interviewer1Info = UserInfoEntity.builder()
                .userAccountInfo(interviewer1)
                .avatar("https://firebasestorage.googleapis.com/v0/b/upload2-23381.appspot.com/o/files%2Fhinh1.jpg?alt=media&token=c16bc73f-a3be-4c01-9f26-5a0d4ccad233")
                .address("21 Đường 379 Tăng Nhơn Phú A, quận 9, TP.HCM")
                .fullName("Nguyễn Interviewer1")
                .gender(Gender.MALE)
                .phone("012334238")
                .build();
        var interviewer2Info = UserInfoEntity.builder()
                .userAccountInfo(interviewer2)
                .avatar("https://firebasestorage.googleapis.com/v0/b/upload2-23381.appspot.com/o/files%2Fhinh1.jpg?alt=media&token=c16bc73f-a3be-4c01-9f26-5a0d4ccad233")
                .address("22 Đường 379 Tăng Nhơn Phú A, quận 9, TP.HCM")
                .fullName("Nguyễn Interviewer2")
                .gender(Gender.MALE)
                .phone("012334238")
                .build();
        var candidateInfo = UserInfoEntity.builder()
                .userAccountInfo(candidate)
                .avatar("https://firebasestorage.googleapis.com/v0/b/upload2-23381.appspot.com/o/files%2Fhinh1.jpg?alt=media&token=c16bc73f-a3be-4c01-9f26-5a0d4ccad233")
                .address("416 Lã Xuân Oai, Long Trường, quận 9, TP.HCM")
                .fullName("Nguyễn Candidate")
                .gender(Gender.MALE)
                .phone("012334488")
                .build();
        var blacklistUserInfo = UserInfoEntity.builder()
                .userAccountInfo(backlistUser)
                .avatar("https://firebasestorage.googleapis.com/v0/b/upload2-23381.appspot.com/o/files%2Fhinh1.jpg?alt=media&token=c16bc73f-a3be-4c01-9f26-5a0d4ccad233")
                .address("22 Lã Xuân Oai, Long Trường, quận 9, TP.HCM")
                .fullName("Nguyễn Blacklist")
                .gender(Gender.MALE)
                .phone("012334488")
                .build();
        userInfoRepository.save(adminInfo);
        userInfoRepository.save(reccer1Info);
        userInfoRepository.save(reccer2Info);
        userInfoRepository.save(interviewer1Info);
        userInfoRepository.save(interviewer2Info);
        userInfoRepository.save(candidateInfo);
        userInfoRepository.save(blacklistUserInfo);
        GenerateRateCompany(reccer1, reccer2);
        GenerateJob(interviewer1,reccer1, reccer2, candidate);
        GenerateEvent(reccer1);
        GenerateBlackList(backlistUser);
    }

    public void GenerateRateCompany(UserAccountEntity reccer1, UserAccountEntity reccer2) {
        var company1 = CompanyEntity.builder()
                .address("Tầng 8, Tòa nhà Vincom Center Đồng Khởi, 72 Lê Thánh Tôn - Phường Bến Nghé - Quận 1 - TP Hồ Chí Minh. ")
                .avatar("https://firebasestorage.googleapis.com/v0/b/quanlytuyendung-4fb2c.appspot.com/o/files%2Fcong-ty-tnhh-buymed-f95dc7cac15325af4367f3c8cf5ee0f6-5ff7dd182c9d8.jpg?alt=media&token=31f94198-61ac-4e6a-896b-3a94017bbcd7")
                .info("thuocsi.vn được thành lập từ năm 2018, là một trong những startup thành công trong lĩnh vực công nghệ về y tế")
                .name("CÔNG TY TNHH BUYMED")
                .phone("0123456776")
                .website("https://thuocsi.vn/")
                .userId(reccer1.getId())
                .build();
        var companyKinhTe = CompanyEntity.builder()
                .address("123 Phố Chợ, Quận Thương Mại, Thành phố Tài Chính")
                .avatar("https://example.com/kinh_te_avatar.jpg")
                .info("Công ty về kinh tế chuyên về nghiên cứu, tư vấn và phát triển về tài chính và thương mại.")
                .name("Công ty Kinh Tế Phát Triển")
                .phone("0987654321")
                .website("https://kinhtecongty.com")
                .userId(reccer2.getId())
                .build();
        companyRepository.save(company1);
        companyRepository.save(companyKinhTe);
    }

    public void GenerateJob(UserAccountEntity interviewer1, UserAccountEntity reccer1, UserAccountEntity reccer2, UserAccountEntity candidate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        var job1 = JobPostingEntity.builder()
                .userAccountEntity(reccer1)
                .name("Thực tập sinh Springboot")
                .position("Intern")
                .language("TOEIC 700")
                .location("Hồ Chí Minh")
                .salary("Thỏa thuận")
                .number("10")
                .workingForm("Full time")
                .sex("Không yêu cầu")
                .experience("Chưa có kinh nghiệm")
                .detailLocation("Số 1 Võ Văn Ngân, Thủ Đức")
                .detailJob("Work on backend services")
                .requirements("Bachelor's degree in Computer Science")
                .interest("Machine learning, web development")
                .image("https://firebasestorage.googleapis.com/v0/b/quanlytuyendung-4fb2c.appspot.com/o/1700823708931_thong-ke-cac-vu-bat-giu-van-chuyen-buon-ban-trai-phep-te-te-tai-viet-nam-tu-nam-2015-2019-nguon-co-quan-tham-quyen-quan-ly-cites-viet-nam-cites-ma.jpg?alt=media")
                .status(true)
                .createDate(sdf.format(java.sql.Date.valueOf(LocalDate.now())))
                .build();
        var job2 = JobPostingEntity.builder()
                .userAccountEntity(reccer1)
                .name("Front-end Developer - ReactJS")
                .position("Mid-level")
                .language("TOEIC 700")
                .location("Hồ Chí Minh")
                .salary("8 - 10 triệu")
                .number("2")
                .workingForm("Toàn thời gian")
                .sex("Không yêu cầu")
                .experience("3 - 5 năm")
                .detailLocation("Hồ Chí Minh: Tòa nhà Bitexco Financial Tower, 2 Hải Triều, quận 1, TPHCM")
                .detailJob("Phát triển và duy trì các ứng dụng web sử dụng ReactJS")
                .requirements("Có kiến thức chuyên sâu về ReactJS và ít nhất 3 năm kinh nghiệm lập trình Front-end")
                .interest("Tham gia vào dự án phát triển các ứng dụng web phức tạp")
                .image("https://www.google.com/url?sa=i&url=https%3A%2F%2Fthtantai2.edu.vn%2Fanh-dep-ve-doi%2F&psig=AOvVaw0HlSMSdWQrghkASCNLrmkx&ust=1696065951309000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCNDsiZbAz4EDFQAAAAAdAAAAABAE")
                .status(true)
                .createDate(sdf.format(java.sql.Date.valueOf(LocalDate.now())))
                .build();

        var jobKinhTe = JobPostingEntity.builder()
                .userAccountEntity(reccer2) // Thay userOfYourCompany bằng thông tin tài khoản người dùng liên quan
                .name("Kế Toán Tài Chính")
                .position("Senior")
                .language("Tiếng Anh chuyên ngành")
                .location("Hà Nội")
                .salary("15 - 20 triệu")
                .number("1")
                .workingForm("Toàn thời gian")
                .sex("Không yêu cầu")
                .experience("5 - 7 năm")
                .detailLocation("Hà Nội: Số 123, Đường ABC, Quận XYZ, Hà Nội")
                .detailJob("Quản lý các hoạt động kế toán, tài chính của công ty")
                .requirements("Có kinh nghiệm quản lý kế toán tài chính, am hiểu về luật thuế và quy định kế toán")
                .interest("Tham gia vào việc phát triển và nâng cao chất lượng kế toán tài chính")
                .image("https://example.com/kinhte_job_image.jpg")
                .status(true)
                .createDate(sdf.format(java.sql.Date.valueOf(LocalDate.now())))
                .build();
        jobPostingRepository.save(job1);
        jobPostingRepository.save(job2);
        jobPostingRepository.save(jobKinhTe);

        GenerateInterview(interviewer1,reccer1, job1, reccer2, jobKinhTe);
        GenerateCV(candidate, job1);


    }


    public void GenerateEvent(UserAccountEntity reccer1) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        var event = EventEntity.builder()
                .article("Sự kiện nổi bật xuyên suốt hàng năm, với mục tiêu kết nối sinh viên trường Đại học Sư phạm kỹ thuật TPHCM với các doanh nghiệp")
                .content("cơ hội việc làm cho sinh viên sắp ra trường")
                .image("https://firebasestorage.googleapis.com/v0/b/quanlytuyendung-4fb2c.appspot.com/o/files%2Fbackground-%C4%91%E1%BA%B9p-hoa-b%C6%B0%C6%A1m-b%C6%B0%E1%BB%9Bm.jpg?alt=media&token=4361512c-e308-40b4-8e9a-c194932d4e66")
                .name("JOB FAIR tại Đại học sư phạm kỹ thuật Tp Hồ Chí Minh")
                .status(true)
                .time(sdf.format(java.sql.Date.valueOf(LocalDate.now())))
                .userAccountEntity(reccer1)
                .build();
        eventRepository.save(event);
    }

    public void GenerateInterview(UserAccountEntity interviewer1, UserAccountEntity reccer1, JobPostingEntity job1, UserAccountEntity reccer2, JobPostingEntity job2) {

        List<UserAccountEntity> listInterview = new ArrayList<>();
        listInterview.add(interviewer1);
        var interview1 = InterviewEntity.builder()
                .description("vòng 1 technical")
                .endDate("2023-12-12T09:00")
                .linkmeet("")
                .roomName("phòng 1")
                .skill("Springboot")
                .startDate("2023-12-10T08:00")
                .status("Created")
                .jobPostingEntity(job1)
                .userAccountEntity(reccer1)
                .interviewers(listInterview)
                .build();

        var interview2 = InterviewEntity.builder()
                .description("vòng 1 tiếng Anh")
                .endDate("2023-12-12T09:00")
                .linkmeet("")
                .roomName("phòng 1")
                .skill("KT")
                .startDate("2023-12-10T08:00")
                .status("Created")
                .jobPostingEntity(job2)
                .userAccountEntity(reccer2)
                .build();
        interviewRepository.save(interview1);
        interviewRepository.save(interview2);

        GenerateInterviewDetail(interview1);
    }


    public void GenerateCV(UserAccountEntity candidate, JobPostingEntity job1) {
        var cv = CVEntity.builder()
                .dateApply("2023-12-09")
                .url("https://firebasestorage.googleapis.com/v0/b/quanlytuyendung-4fb2c.appspot.com/o/1702126839381_cv1.pdf?alt=media")
                .jobPostingEntity(job1)
                .userAccountEntity(candidate)
                .build();
        cvRepository.save(cv);
    }


    public void GenerateSkillPosition() {
        var skill1 = SkillEntity.builder()
                .createdBy("admin@gmail.com")
                .isDeleted(false)
                .skillName("ReactJS")
                .updateTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .build();
        var skill2 = SkillEntity.builder()
                .createdBy("admin@gmail.com")
                .isDeleted(false)
                .skillName("Java Springboot")
                .updateTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .build();
        skillRepository.save(skill1);
        skillRepository.save(skill2);
        var position1 = PositionEntity.builder()
                .createdBy("admin@gmail.com")
                .isDeleted(false)
                .positionName("Intern")
                .updateTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .build();
        var position2 = PositionEntity.builder()
                .createdBy("admin@gmail.com")
                .isDeleted(false)
                .positionName("Fresher")
                .updateTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .build();
        var position3 = PositionEntity.builder()
                .createdBy("admin@gmail.com")
                .isDeleted(false)
                .positionName("Middle")
                .updateTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .build();
        positionRepository.save(position1);
        positionRepository.save(position2);
        positionRepository.save(position3);


    }

    public void GenerateQuestions() {
        List<QuestionEntity> questions = new ArrayList<>();
        questions.add(QuestionEntity.builder()
                .question("Tại sao ReactJS được ưa chuộng trong phát triển web?")
                .answer("ReactJS cung cấp hiệu suất cao, linh hoạt và dễ bảo trì với Virtual DOM, JSX và mô hình Component.")
                .createTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .createdBy("interviewer1@gmail.com")
                .field(FieldEnum.TechSkill)
                .isDeleted(false)
                .build());
        questions.add(QuestionEntity.builder()
                .question("Lifecycle của React Component có những phương thức nào và ý nghĩa của chúng là gì?")
                .answer("Ví dụ như componentDidMount, componentDidUpdate, và componentWillUnmount, mỗi phương thức có vai trò và mục đích khác nhau trong quá trình lifecycle của một Component.")
                .createTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .createdBy("interviewer1@gmail.com")
                .field(FieldEnum.TechSkill)
                .isDeleted(false)
                .build());
        questions.add(QuestionEntity.builder()
                .question("Spring Boot là gì và tại sao nó được ưa chuộng trong phát triển ứng dụng Java?")
                .answer("Spring Boot là một framework giúp tạo và triển khai ứng dụng Java dễ dàng và nhanh chóng với cấu hình mặc định. Nó giảm độ phức tạp khi phát triển ứng dụng Java.")
                .createTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .createdBy("interviewer1@gmail.com")
                .field(FieldEnum.TechSkill)
                .isDeleted(false)
                .build());
        questions.add(QuestionEntity.builder()
                .question("Giải thích Dependency Injection trong Spring Boot?")
                .answer("Dependency Injection là một kỹ thuật cho phép Spring cung cấp các đối tượng mà một đối tượng cần để thực hiện chức năng của nó. Nó giúp giảm sự phụ thuộc giữa các lớp trong ứng dụng.")
                .createTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .createdBy("interviewer1@gmail.com")
                .field(FieldEnum.TechSkill)
                .isDeleted(false)
                .build());
        questions.add(QuestionEntity.builder()
                .question("Điểm mạnh của ngôn ngữ lập trình Python là gì?")
                .answer("Python có cú pháp đơn giản, đa năng và mạnh mẽ với nhiều thư viện hỗ trợ, rất phù hợp cho các ứng dụng web, khoa học dữ liệu và AI.")
                .createTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .createdBy("interviewer1@gmail.com")
                .field(FieldEnum.TechSkill)
                .isDeleted(false)
                .build());
        questions.add(QuestionEntity.builder()
                .question("Có những cách nào để định nghĩa hàm trong Python?")
                .answer("Hàm có thể được định nghĩa bằng từ khóa 'def' và có thể chứa các đối số và câu lệnh bên trong.")
                .createTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .createdBy("interviewer1@gmail.com")
                .field(FieldEnum.TechSkill)
                .isDeleted(false)
                .build());
        questions.add(QuestionEntity.builder()
                .question("Python decorators là gì và cách chúng hoạt động?")
                .answer("Decorators là một cú pháp cho phép thay đổi hoặc mở rộng hành vi của hàm hoặc lớp trong Python. Chúng thường được sử dụng để thực hiện các thay đổi trước và sau khi gọi hàm.")
                .createTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .createdBy("interviewer1@gmail.com")
                .field(FieldEnum.TechSkill)
                .isDeleted(false)
                .build());
        questions.add(QuestionEntity.builder()
                .question("Giải thích ý nghĩa của các chế độ mở file trong Python?")
                .answer("Có ba chế độ mở file chính trong Python: 'r' (đọc), 'w' (ghi - xóa nếu file đã tồn tại), 'a' (ghi - giữ nguyên nếu file đã tồn tại).")
                .createTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .createdBy("interviewer1@gmail.com")
                .field(FieldEnum.TechSkill)
                .isDeleted(false)
                .build());
        questions.add(QuestionEntity.builder()
                .question("Làm thế nào để xử lý exceptions (ngoại lệ) trong Python?")
                .answer("Exceptions trong Python có thể được xử lý bằng cú pháp 'try', 'except', 'finally'. Các lệnh trong khối 'try' được thực thi và nếu có lỗi, nó sẽ được xử lý trong khối 'except'.")
                .createTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .createdBy("interviewer1@gmail.com")
                .field(FieldEnum.TechSkill)
                .isDeleted(false)
                .build());
        questions.add(QuestionEntity.builder()
                .question("Có sự khác biệt gì giữa List và Tuple trong Python?")
                .answer("List và Tuple là hai kiểu dữ liệu lưu trữ nhiều giá trị khác nhau. Một điểm khác biệt chính là List có thể thay đổi giá trị, trong khi Tuple là không thể thay đổi (immutable).")
                .createTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .createdBy("interviewer1@gmail.com")
                .field(FieldEnum.TechSkill)
                .isDeleted(false)
                .build());
        questions.add(QuestionEntity.builder()
                .question("Bạn có thể mô tả về một tình huống khi bạn phải làm việc trong một nhóm có ý kiến trái chiều?")
                .answer("Một câu trả lời có thể bao gồm việc nêu ra tình huống cụ thể, cách giải quyết xung đột và cách học hỏi từ những ý kiến khác nhau.")
                .createTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .createdBy("interviewer1@gmail.com")
                .field(FieldEnum.SoftSkill)
                .isDeleted(false)
                .build());
        questions.add(QuestionEntity.builder()
                .question("Làm thế nào để bạn quản lý thời gian hiệu quả?")
                .answer("Câu trả lời có thể liệt kê các phương pháp quản lý thời gian như tạo lịch trình, ưu tiên công việc, và sử dụng công cụ hỗ trợ.")
                .createTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .createdBy("interviewer1@gmail.com")
                .field(FieldEnum.SoftSkill)
                .isDeleted(false)
                .build());
        questions.add(QuestionEntity.builder()
                .question("Làm thế nào để bạn giải quyết một vấn đề phức tạp?")
                .answer("Câu trả lời có thể bao gồm việc phân tích vấn đề, tìm kiếm thông tin, tư duy sáng tạo và thử nghiệm các giải pháp khác nhau.")
                .createTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .createdBy("interviewer1@gmail.com")
                .field(FieldEnum.SoftSkill)
                .isDeleted(false)
                .build());
        questions.add(QuestionEntity.builder()
                .question("Làm thế nào bạn xử lý áp lực và căng thẳng trong công việc?")
                .answer("Câu trả lời có thể bao gồm việc tìm kiếm cách thức giảm bớt áp lực, quản lý căng thẳng thông qua việc tập thể dục, thực hành mindfulness hoặc quản lý công việc một cách hiệu quả hơn.")
                .createTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .createdBy("interviewer1@gmail.com")
                .field(FieldEnum.SoftSkill)
                .isDeleted(false)
                .build());
        questions.add(QuestionEntity.builder()
                .question("Bạn có thể mô tả về một lần bạn phải đưa ra quyết định quan trọng trong công việc?")
                .answer("Câu trả lời có thể bao gồm mô tả tình huống cụ thể, cách bạn thu thập thông tin, xác định các lựa chọn và quyết định của bạn.")
                .createTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .createdBy("interviewer1@gmail.com")
                .field(FieldEnum.SoftSkill)
                .isDeleted(false)
                .build());
        questions.add(QuestionEntity.builder()
                .question("What are some effective ways to improve English language proficiency?")
                .answer("Answers may include practicing regularly, reading books, watching English movies, and engaging in conversations with native speakers.")
                .createTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .createdBy("interviewer1@gmail.com")
                .field(FieldEnum.Language)
                .isDeleted(false)
                .build());
        questions.add(QuestionEntity.builder()
                .question("Can you discuss the importance of English in the global business environment?")
                .answer("Responses might include how English facilitates international communication, enhances job opportunities, and enables access to global resources.")
                .createTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .createdBy("interviewer1@gmail.com")
                .field(FieldEnum.Language)
                .isDeleted(false)
                .build());

        questions.add(QuestionEntity.builder()
                .question("How would you describe your experience with English language learning?")
                .answer("This question allows the interviewee to share their personal journey, challenges faced, and strategies used to improve their English skills.")
                .createTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .createdBy("interviewer1@gmail.com")
                .field(FieldEnum.Language)
                .isDeleted(false)
                .build());

        questions.add(QuestionEntity.builder()
                .question("Can you explain the importance of effective communication in English within a professional setting?")
                .answer("Answers might touch on clear communication, avoiding misunderstandings, and building rapport with colleagues or clients.")
                .createTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .createdBy("interviewer1@gmail.com")
                .field(FieldEnum.Language)
                .isDeleted(false)
                .build());

        questions.add(QuestionEntity.builder()
                .question("What methods do you use to expand your vocabulary in English?")
                .answer("Responses could include reading diverse materials, using vocabulary apps, and practicing new words in conversations or writing.")
                .createTime(LocalDateTime.now().atOffset(ZoneOffset.ofHours(7)).toLocalDateTime())
                .createdBy("interviewer1@gmail.com")
                .field(FieldEnum.Language)
                .isDeleted(false)
                .build());

        for (QuestionEntity question : questions) {
            questionRepository.save(question);
        }
    }

    public void GenerateBlackList(UserAccountEntity user)  {
        var blacklist = BlacklistEntity.builder()
                .dateBlacklist(LocalDateTime.now())
                .description("hacker")
                .userAccountEntity(user)
                .build();
        blackListRepository.save(blacklist);
    }


    public void GenerateInterviewDetail(InterviewEntity interview) {
        var detail = InterviewDetailEntity.builder()
                .averageScore(0F)
                .candidateId(6L)
                .comment("")
                .date("2023-12-10")
                .description("no description")
                .englishQuestions("")
                .softSkillQuestions("")
                .interviewer("")
                .technicalQuestions("")
                .status("Chưa phỏng vấn")
                .interviewerEmail("")
                .time("08h00 to 09h00")
                .interview(interview)
                .build();
        interviewDetailRepository.save(detail);
    }




}
