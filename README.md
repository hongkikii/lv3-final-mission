# lv3-final-mission
## 구현 내용 정리

### 1. 요구사항별 정리
    - [x]  애플리케이션을 AWS에 배포 가능한 상태로 구축해야 한다.
    - [x]  애플리케이션 서버와 데이터베이스 서버를 물리·네트워크 계층에서 분리해야 한다.
    - [x]  개발(dev)과 운영(prod)의 설정을 서로 독립적으로 관리·배포해야 한다.
    - [x]  소스 변경 시 자동 빌드·배포가 수행되도록 파이프라인을 구성해야 한다.
    - [x]  운영 중 스키마 변경 시 데이터 보존 상태로 마이그레이션 및 재배포를 완료해야 한다.
    - [ ]  개인 도메인을 애플리케이션에 연결하고 HTTPS를 적용해야 한다.
    - [ ]  애플리케이션 로그를 파일 또는 수집기로 영속화하고 조회 절차를 제공해야 한다.
    - [ ]  서버 자원(CPU·메모리·네트워크 등)을 지표·대시보드로 시각화해야 한다.
    - [ ]  제공 API의 문서 명세를 작성·공개해야 한다.
    - [ ]  전체 인프라를 다이어그램으로 작성하고 각 구성요소 역할을 설명해야 한다.
### 2. 원격 배포 가능한 상태 만들기
    1. 애플리케이션
        1. Docker를 사용하여 환경이 바뀔 때마다 쉽게 배포할 수 있도록 하였습니다.
    2. DB
        1. Mysql을 사용하였다.
        2. 마찬가지로 Docker 이미지를 사용하여  환경이 바뀔 때마다 쉽게 배포할 수 있도록 하였습니다.
        3. 이때 볼륨 데이터를 별도로 설정하여 컨테이너를 종료하여도 데이터가 삭제되지 않도록 하였습니다.
### 3. 환경 분리하기
    1. 로컬 환경
        1. 테스트 코드는 빠른 실행을 위해 h2를 사용하였습니다.
        2. 애플리케이션 실행 시에는 원격 서버와 유사한 환경을 구성하기 위해 Docker를 통해 애플리케이션과 DB를 실행하였습니다.
    2. 개발 환경
        1. 하나의 EC2 내에서 애플리케이션과 DB를 모두 실행하였습니다.
        2. EC2는 public subnet에 위치하고 있습니다.
        3. 하나의 EC2를 사용하기에 비용을 절감할 수 있습니다.
        4. 다만 서버에 문제가 생길 시 애플리케이션과 DB 모두 영향을 받는다는 문제가 있고, 이후 성능 테스트가 필요하다면 운영 환경과 유사한 설정이 필요할 것이라 생각합니다.
    3. 운영 환경
        1. 애플리케이션용 EC2는 public subnet에, DB용 EC2는 private subnet에 생성하였습니다.
        2. DB는 bastion host를 통해 접속합니다.
        3. 이를 통해 DB를 완전히 격리시켜 외부 공격을 차단할 수 있습니다.
    4. 환경 변수
        1. 환경변수를 사용해 보안이 필요한 데이터는 외부에서 읽을 수 없도록 하였습니다.
### 4. 스키마 설정
    1. JPA ddl-auto 설정은 validate로 설정해 스키마가 유효한지 검사합니다.
    2. 이를 통해 애플리케이션 코드 레벨에서 예상치 못한 스키마의 변경을 막을 수 있습니다.
    3. 또한 로컬과 개발 환경에서 사전적으로 스키마를 변경해보고 유효성을 판단해 운영 환경에서 발생할 수 있는 문제를 최소화할 수 있습니다.
    4. 초기 ****`ddl.sql`은 아래와 같이 문서화하고, 각 서버에 적용해주었습니다.

    create table users (
        id bigint not null auto_increment,
        name varchar(255) not null,
        primary key (id)
    );
    
    create table restaurants (
        id bigint not null auto_increment,
        name varchar(255) not null,
        primary key (id)
    );
    
    create table times (
        id bigint not null auto_increment,
        start_at time not null,
        primary key (id)
    );
    
    create table restaurant_schedules (
        id bigint not null auto_increment,
        is_available bit not null,
        restaurant_id bigint not null,
        date date not null,
        time_id bigint not null,
        primary key (id),
        constraint fk_schedule_restaurant
            foreign key (restaurant_id) references restaurants (id),
        constraint fk_schedule_time
            foreign key (time_id) references times (id)
    );
    
    create table reservations (
        id bigint not null auto_increment,
        restaurant_schedule_id bigint not null,
        user_id bigint not null,
        created_at datetime not null,
        primary key (id),
        constraint fk_reservation_schedule
            foreign key (restaurant_schedule_id) references restaurant_schedules (id),
        constraint fk_reservation_user
            foreign key (user_id) references users (id),
        constraint uq_reservation_schedule unique (restaurant_schedule_id)
    );

### 5. ci-cd 파이프라인 만들기
    1. 수동 배포 시 매번 EC2에 접속해야 한다는 번거로움이 있습니다.
    2. 배포를 할 줄 아는 팀원이 없다면 배포가 불가능할 수도 있습니다.
    3. 로컬과 원격 환경이 달라 로컬에서는 잘 되던 빌드가 원격에서는 되지 않을 수도 있어서 확인이 필요합니다.
    4. 위와 같은 이유로 ci-cd 파이프라인을 구성해 자동화 작업을 해주었습니다.
