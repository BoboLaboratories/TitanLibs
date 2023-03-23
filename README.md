## TODO: Idenfifier & AbstractRegistry
## TODO: Formatter

<br />
<div align="center">
<h2 align="center">Titanet Libs</h2>
  <p align="center">
    Una libreria contenenete classi e utils usate spesso
    <br />
  </p>
</div>

<details>
  <summary>Moduli</summary>
  <ol>
    <li><a href="#database">Databases</a></li>
    <li><a href="#configs">Configurations</a></li>
    <li><a href="#message">Messages</a></li>
  </ol>
</details>

-----------

## Prima di iniziare
### Interfaccia Relodable
L'interfaccia Reloadable permette di gestire velocemente l'abilitazione e la disabilitazione di ciascun modulo, tuttavia rende necessario
prestare più attenzione durante i metodi ```onEnable()``` e ```onDisable()```.

L'uso corretto è elencato di seguente usando come esempio la classe ```ConfigManager```, che implementa l'interfaccia

```java
  private ConfigManager<ExampleEnum> configManager;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager<>(this, ExampleEnum.class);
        configManager.onEnable();
    }
    
    @Override
    public void onDisable() {
        if (configManager != null) {
            configManager.onDisable();
            configManager = null;
        }
    }
```
-----------

## ConfigManager <a name="configs"></a>
### Creazione del ConfigManger
Per creare un ```ConfigManager``` è necessario prima costruire un ```Enum``` 
che specifica il percorso file di ogni file di configurazione, come nell'esempio seguente.

```java
public enum ExampleEnum implements IConfig {

    ROOT("config.yml"),
    CONFIG_1("configs/config_1.yml"),
    CONFIG_2("configs/config_2.yml");
    
    private final String path;
    
    ExampleEnum(String path) {
        this.path = path;
    }

    @Override
    public @NotNull String getPath() {
        return path;
    }
}
```

## Utilizzo

```java
ConfigManager<ExampleEnum> configManager = new ConfigManager<>(this, ExampleEnum.class);
FileConfiguration config = configManager.getConfig(ExampleEnum.ROOT);
```
In seguito è possibile utilizzare l'oggetto FileConfiguration normalmente.

-----------

## Database


### Creazione del database <a name="database"></a>

Le query per la creazione del database necessitano di essere scritte nel file ```resources/schema.sql```
è possibile vederne un esempio <a href="https://github.com/TitanetMC/TitanLibs/blob/main/src/main/resources/schema.sql">QUI</a>.

#### Nota: Il commento ```-- SEPARATOR --``` a fine di ogni Query è strettamente necessario.

Esclusivamente il database MySQL può disporre di una configurazione, trovabile <a href="https://github.com/TitanetMC/TitanLibs/blob/main/src/main/resources/config.yml">QUI</a>.

## Utilizzo
### H2
```java
File database = new File(getDataFolder().getAbsolutePath(), 'database_name');
Database database = new H2Database(file);
database.onEnable();
```
### MySQL
```java
FileConfiguration rootConfig = configManager.getConfig(Configs.ROOT);
ConfigurationSection dbSettings = rootConfig.getConfigurationSection("database");
Database database = new MysqlDatabase(dbSettings);
```

Successivamente sarà possibile eseguire query normalmente 
a prescindere dal tipo di database usato, come in questo caso

```java
try (Connection connection = database.getConnection()) {
    try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY)) {
        // DO STUFF
    }
} catch (SQLException e) {
    throw new RuntimeException(e);
}
```
-----------

## Messages <a name="message"></a>
### Creazione del MessageManager

Prima di poter creare o inviare messaggi è necessario istanziare un ```MessageManager```, per farlo serve
che la classe che estende ```JavaPlugin``` implementi l'interfaccia ```Messageable```, il
risultato dovrà essere come nel seguente esempio.

```java
public final class ExamplePlugin extends JavaPlugin {

    private MiniMessage miniMessage;
    private BukkitAudiences audience;

    @Override
    public void onEnable() {
        // Plugin startup logic
        audience = BukkitAudiences.create(this);
        miniMessage = MiniMessage.miniMessage();
        // Nuova istanza del MessageManager
        MessageManager messageManager = new MessageManager(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
```

È possibile fornire al ```MessageManager``` un config di default da dove prendere i messaggi
```java
FileConfiguration lang = configManager.getConfig(Configs.LANG);
MessageManager messageManager = new MessageManager(this, lang);
```
### Creazione dei messaggi
Una volta istanziato il ```MessageManager``` è possibile creare nuovi messaggi.
Si possono creare a partire da una key nel config di default (istanziabile come visto prima) oppure da una stringa.

```java
Message keyMessage = messageManager.fromKey("some-key");

// In questo caso i messaggi verranno inviati su linee separate
Message linesMessage = messageManager.fromLines("from", "some", "lines");
```

### Formattazione
Tutti i messaggi supportano MiniMessages, quindi è possibile applicare colori
e gradient sfruttando il formato di quella libreria, ulteriori informazioni 
possono essere trovate <a href="https://docs.adventure.kyori.net/minimessage/format.html#format">QUI</a>

### Replace
È possibile fare un replace di un placeholder all'interno di un messaggio usando il metodo
```replace()``` sulla classe message, come nel caso seguente.
```java
Message message = messageManager.fromLines("Il player %player% non ha abbastanza soldi");
message.replace("%player%", "Staminal");

Player staminal = Bukkit.getPlayer("Staminal");
messageManager.send(message, staminal);

// L'output sarà: Il player Staminal non ha abbastanza soldi
```
