# Relatórios de Feedback

Serviço responsável por enviar relatórios de feedbacks coletados nos últimos 7 dias.

Foi desenvolvido para ser utilizado como uma função lambda na AWS. É necessário configurar um e-mail no SES da AWS.


## Instalação

1 - Clone o repositório:
```bash
git clone https://github.com/raclug/feedback-report.git
cd feedback-report
```

2 - Gere o artefato jar:
```bash
./mvnw clean package
```

3 - Faça o deploy na AWS Lambda:

- Crie uma nova função Lambda na AWS.

- Faça o upload do arquivo `target/avalidacao-relatorio-service-0.0.1-SNAPSHOT-aws.jar`

- Configure o handler como `org.springframework.cloud.function.adapter.aws.FunctionInvoker`

- Adicione as variáveis de ambiente necessárias: `EMAIL_FROM e EMAIL_TO` com os e-mails configurados no SES e as variáveis `SPRING_DATASOURCE_PASSWORD, SPRING_DATASOURCE_URL e SPRING_DATASOURCE_USERNAME` com os dados do banco utilizado para armazenar os feedbacks. 

- Adicione a Lambda na mesma VPC onde o banco de dados está hospedado.

- Na role da lambda adicine uma política com permissão para enviar e-mails via SES.

- Configure um gatilho no EventBridge com a cron desejada para executar a função semanalmente.
