FROM eclipse-temurin:17-jre-alpine AS builder

COPY ./target/todolist-2-layer-be-1.0.0.jar ./app.jar

RUN java -Djarmode=layertools -jar ./app.jar extract


FROM eclipse-temurin:17-jre-alpine

COPY --from=builder dependencies/ ./

COPY --from=builder snapshot-dependencies/ ./

COPY --from=builder spring-boot-loader/ ./

COPY --from=builder application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
