# 后端 Dockerfile
FROM eclipse-temurin:17-jdk

WORKDIR /app

# 复制 JAR 包
COPY target/PartTimeGo-0.0.1-SNAPSHOT.jar app.jar

# 暴露端口
EXPOSE 9090

# 启动命令
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
