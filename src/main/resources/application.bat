server.port=8083
eureka.client.serviceUrl.defaultZone = http://192.168.29.137:8761/eureka
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.client.healthcheck.enabled=true
eureka.client.instance-info-replication-interval-seconds=30
spring.application.name=login-service
management.info.env.enabled=true
management.endpoints.web.exposure.include=*

# kafka nodes separated by comma
spring.kafka.bootstrap-servers=192.168.29.137:29092,192.168.29.137:39092
spring.kafka.consumer.group-id=consumer_group1
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.enable-auto-commit=false
spring.kafka.listener.ack-mode=manual
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.max.poll.interval.ms=30000
hibernate.transaction.jta.platform=enable
logging.level.root=INFO
spring.main.allow-circular-references=true
spring.datasource.url=jdbc:mariadb://192.168.29.137:3307/login
# spring.datasource.url=jdbc:mysql://inventoryapp.mysql.database.azure.com:3306/inventory?useSSL=true&sslMode=REQUIRED&serverTimezone=UTC
spring.datasource.username=ravi
spring.datasource.password=ravi
spring.datasource.hikari.minimum-idle=10
spring.datasource.hikari.maximum-pool-size=30000
#logging.level.org.springframework.data.jpa=DEBUG
spring.jpa.show-sql = false
spring.jpa.properties.hibernate.format_sql=false
spring.jpa.hibernate.ddl-auto = update
#spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQLDialect
#logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql=TRACE
#hibernate.id.new_generator_mappings=true

# To pretty print JSON output

# To ignore fields with null values

# To disable writing dates as timestamps
spring.jackson.serialization.write-dates-as-timestamps=false

# To fail if a JSON contains a property not present in the POJO
spring.jackson.deserialization.fail-on-unknown-properties=true
