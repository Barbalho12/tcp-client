## TCP Client

Esta aplicação pode ser executada como apoio ao [Servidor TCP](https://github.com/Barbalho12/tcp-server).

### Enviar clientes

Crie um arquivo `client.js` para armazenar o cliente :

```js
{
    "idade":18,
    "peso":67,
    "altura":167,
    "nome":"Marcos"
}

```

Envie especificando o `ip do servidor`, a `porta`, o tipo de frame `-cliente` e o caminho absoluto para o `arquio json`

```bash
./run.sh localhost 9999 -cliente cliente.js
```

### Envia mensagem de texto

Envie especificando o `ip do servidor`, a `porta`, o tipo de frame `-texto` e a mensagem

```bash
./run.sh localhost 9999 -texto "Mensagem de texto"
```

### Reguisitar data e hora

Envie especificando o `ip do servidor`, a `porta`, o tipo de frame `-datetime` e a zona 

```bash
./run.sh localhost 9999 -datetime "America/Sao_Paulo"
```

### Log de mensagens

As requisições de recebimento e envio de mensagens são logdadas em `CLIENT_LOG.txt`

### Referências

- [CRC8](https://www.javatips.net/api/xtrememp-swing-master/xtrememp-audio-spi-flac/src/org/kc7bfi/jflac/util/CRC8.java)