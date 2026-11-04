# Histórias do Usuário

| # | História | Critérios de aceite |
|---|---|---|
| US01 | Como **cliente**, quero me cadastrar para poder usar o sistema. | CPF com 11 dígitos e único; até 3 rendimentos; usuário único; senha ≥ 6 caracteres |
| US02 | Como **usuário**, quero fazer login com usuário e senha para acessar o sistema. | Credenciais inválidas retornam erro; rotas privadas exigem token |
| US03 | Como **cliente**, quero consultar, alterar e excluir meu cadastro. | Alterações validadas; excluir remove também meus pedidos |
| US04 | Como **cliente**, quero criar um pedido de aluguel escolhendo um automóvel e o período. | Data final > inicial; status inicial "Em análise" |
| US05 | Como **cliente**, quero acompanhar o status dos meus pedidos. | Lista mostra apenas pedidos do próprio cliente; detalhe mostra a trilha do pedido |
| US06 | Como **cliente**, quero modificar um pedido ainda em análise. | Após a análise, o cliente não edita mais |
| US07 | Como **cliente**, quero cancelar um pedido. | Pedido executado não pode ser cancelado |
| US08 | Como **agente**, quero ver todos os pedidos para analisá-los. | Lista completa com nome do cliente |
| US09 | Como **agente**, quero registrar parecer financeiro positivo ou negativo. | Parecer textual obrigatório; só pedidos em análise |
| US10 | Como **agente**, quero executar o contrato de pedidos aprovados. | Define propriedade do automóvel (cliente, empresa ou banco) |
| US11 | Como **banco**, quero conceder crédito vinculado ao contrato. | Valor, juros mensais e parcelas; só se o cliente solicitou |
| US12 | Como **usuário**, quero cadastrar automóveis (matrícula, ano, marca, modelo, placa). | Placa e matrícula únicas |
