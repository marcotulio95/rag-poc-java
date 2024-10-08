## Descrição

No projeto, desenvolvemos uma aplicação Java com fins educativos, cujo objetivo é coletar entradas do usuário e enviar essas informações para as APIs da OpenAI, utilizando um modelo de LLM para gerar respostas.

Além disso, aprendemos a armazenar um arquivo PDF com informações do perfil de LinkedIn de Marco Túlio Macedo Rodrigues em um banco de dados vetorial. Isso permite enriquecer as respostas do modelo LLM da OpenAI com dados específicos que não estão publicos na internet.

## APIs disponíveis no projeto

* `http://localhost:8080/chat?message=somMessageHere`
* `http://localhost:8080/chat-metada?message=somMessageHere`
* `http://localhost:8080/chat-template?message=somMessageHere`
* `http://localhost:8080/chat-template-rag?message=someMessageHere`
