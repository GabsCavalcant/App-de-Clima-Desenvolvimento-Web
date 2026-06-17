# Entrega 1 — Configuração do projeto e banco SQLite (Room)

## O que está incluído
- `PrevisaoTempo.kt` — entidade (tabela `previsoes`) com os campos típicos de uma previsão do tempo.
- `PrevisaoDao.kt` — operações de inserção, consulta e remoção.
- `AppDatabase.kt` — classe Room que cria/gerencia o banco SQLite `previsao_tempo_db`.
- `PrevisaoDaoTest.kt` — teste instrumentado que comprova que o banco funciona (inserção e consulta).

## Como integrar no seu projeto Android Studio
1. Copie a pasta `data/local` (com os 3 arquivos `.kt`) para dentro de
   `app/src/main/java/<seu_pacote>/`.
2. Copie `PrevisaoDaoTest.kt` para `app/src/androidTest/java/<seu_pacote>/data/local/`.
3. Se o seu pacote não for `br.edu.ifsp.weatherapp`, ajuste a linha `package ...`
   no topo de cada arquivo para o pacote correto do seu projeto.
4. Adicione as dependências do Room e do KSP no `build.gradle.kts` do módulo `app`
   (snippet completo na mensagem de entrega).
5. Sincronize o Gradle ("Sync Now").

## Como validar que o banco foi criado corretamente
1. Clique com o botão direito em `PrevisaoDaoTest.kt` → "Run 'PrevisaoDaoTest'".
2. Os dois testes devem passar (✅), confirmando que o Room conseguiu criar o
   esquema do banco e que inserção/consulta funcionam.

## Próximas entregas
- Entrega 2: tela de clima atual e previsão (consumindo `PrevisaoDao` via `Flow`).
- Entrega 3: integração com a API RESTful (Retrofit) para popular o banco.
- Entrega 4: já estará coberta pela estrutura criada aqui — o Room **é** o
  armazenamento offline.
- Entrega 5: testes finais e ajustes de usabilidade.
