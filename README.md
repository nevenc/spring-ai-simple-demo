# spring-ai-simple-demo

Spring AI simple demo:
* Examples with Ollama and OpenAI

## Configure app with Ollama

* Make sure you have `ollama` installed locally

* Run `ollama` in a terminal, e.g.
```shell
ollama start
```

* Download a model in another terminal (llama3.2 is about 4Gb), e.g.
```shell
ollama pull llama3.2
```

* Run the model in another terminal, e.g.
```shell
ollama run llama3.2
```

* Make sure you have configured the Spring AI Spring Boot starter for Ollama, e.g.
```xml
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-starter-model-ollama</artifactId>
    </dependency>
```

## Configure app with OpenAI

* Make sure you have OpenAI API key created and configured, e.g.
```shell
export OPENAI_API_KEY="sk-proj-NV..."
```
* Make sure you have configured the Spring AI Spring Boot starter for OpenAI, e.g.
```xml
    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-starter-model-openai</artifactId>
    </dependency>
```

## Run the application

* Start the application, e.g.
```
./mvnw spring-boot:run
```

## Examples

* HelloWorld example
```shell
http http://localhost:8080/
```

* System Instructions (default)
```shell
http http://localhost:8080/system
```

* System Instructions with a query
```shell
http 'http://localhost:8080/system?query="tell me a joke"'
```

* Prompt Stuffing (default)
```shell
http http://localhost:8080/prompt-stuffing
```

* Prompt Stuffing with a topic
```shell
http 'http://localhost:8080/prompt-stuffing?topic="Spring AI"'
```

* RAG - Load the vector store
```shell
http http://localhost:8080/load-pdf
```

* RAG - Ask the question about the provided data
```shell
http 'http://localhost:8080/ask-about-pdf?topic="What is this article about?"'
```

* RAG - Ask the question about the provided data (newer RAG library)
```shell
http http://localhost:8080/rag
```

* Chat Memory - Hello
```shell
http 'http://localhost:8080/memory?query="Hi, my name is Neven"'
```

* Chat Memory - Hello again
```shell
http 'http://localhost:8080/memory?query="What is my name again?"'
```

* Tool Calling
```shell
http 'http://localhost:8080/tool?query="I am traveling from Stockholm to Barcelona today. What kind of clothes should I pack?"'
```
