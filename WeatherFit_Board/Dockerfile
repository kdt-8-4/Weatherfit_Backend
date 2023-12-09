FROM openjdk:17-jdk

# 이미지 내부에 /app 디렉토리를 작업 디렉토리로 설정합니다.
WORKDIR /app

# 현재 디렉토리의 build/libs 디렉토리에 있는 .jar 파일을 컨테이너에 복사합니다.
COPY build/libs/*.jar app.jar

# 컨테이너가 시작될 때 실행될 명령어를 설정합니다.
ENTRYPOINT ["java","-Duser.timezone=Asia/Seoul","-jar","app.jar"]