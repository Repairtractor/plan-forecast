FROM openjdk:8-jdk-alpine
# 使用dejdk
## saltice-sc-admin-1.0-SNAPSHOT.jar是我项目打包后生成的可运行的jar
## saltice-sc.jar 是在构建镜像里面的名称，就是把前面那个文件从上下文目录放到了镜像里面的根目录去了
ADD plan-forecast-api-0.0.1-SNAPSHOT.jar app.jar
#ADD Sop-Open-API-Monitor-Agent-1.0-SNAPSHOT.jar agent.jar

MAINTAINER SaltIce

## vm 命令进行组装，其中-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5050为远程调试的参数
ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5050","-Dfile.encoding=utf-8","-Dspring.redis.password=root","-jar","/app.jar"]