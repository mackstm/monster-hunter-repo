<div align="justify">

# Monster Hunter: Version 1

## Índice
- [Descripción del juego](#index01)
- [Diseño](#index02)
- [Programa](#index03)

## Descripción del juego <a name="index01"></a>

La tarea consiste en un juego cuyo objetivo es que cada cazador debe cazar los monstruos del mapa en un tiempo limitado. Los cazadores se mueven
aleatoriamente por el mapa y caza al monstruo que se encuentre en su posición actual.

## Diseño <a name="index02"></a>

El programa consta de 4 clases: [Hunter](src/main/java/es/ies/puerto/model/Hunter.java), [GameMap](src/main/java/es/ies/puerto/model/GameMap.java), 
[Monster](src/main/java/es/ies/puerto/model/Monster.java) y [RunHunt](src/main/java/es/ies/puerto/model/RunHunt.java).

La clase Hunter contiene los datos de los cazadores y extiende de Thread, ya que los cazadores serán los hilos que se mueven por el mapa.

Los monstruos de la clase Monster aún no tienen movimiento implementado en esta versión y, por tanto, no extienden de Thread.

La clase GameMap contiene el mapa y las funciones sincronizadas que mueven a los cazadores y a los monstruos.

La clase RunHunt tiene el programa principal.

## Programa <a name="index03"></a>

Para explicar el funcionamiento del programa empezaremos por GameMap.

```java
public class GameMap {
    private int size;
    private ConcurrentHashMap<String, String> areas;
    private String[][] map;
    private List<Hunter> hunters;
    private List<Monster> monsters;
}
```

El mapa, al ser cuadrado, solo necesita un atributo size. En cuanto a la diferencia entre las propiedades areas y map:

- `areas`: Es un concurrent hashmap que contiene internamente las posiciones de los cazadores y los monstruos. La clave es el nombre del cazador o monstruo, y el valor es la posición en sí, en formato x,y.

- `map`: Es una matriz de caracteres que representa el mapa. Es puramente visual para visualizar el mapa. Las posiciones vacías se representan con asterisco "*".

GameMap es el recurso que comparten Hunter y Monster (aunque en esta versión Monster no lo utiliza). Por tanto, es la clase que contiene las funciones sincronizadas.

Para la captura de los monstruos opté por aprovechar el mapa visual y ver qué hay en la posición a la que se dirije el cazador.

```java
public synchronized void moveHunter(Hunter hunter) {
    Random random = new Random();
    int x = random.nextInt(size);
    int y = random.nextInt(size);

    String[] position = hunter.getPosition().split(",");
    switch (map[x][y]) {
        case "*" -> {
            map[x][y] = "H";
            map[Integer.parseInt(position[0])][Integer.parseInt(position[1])] = "*";
            hunter.setPosition(x + "," + y);
        }
    
        case "M" -> {
            huntMonster(monsters, hunter);
            map[x][y] = "H";
            map[Integer.parseInt(position[0])][Integer.parseInt(position[1])] = "*";
            hunter.setPosition(x + ","+ y);
        }
        
        case "H" -> moveHunter(hunter);
    }
}
```

Finalmente, el método run del cazador.

```java
public void run() {
    long initialTime = System.currentTimeMillis();
    long timePassed = 0;
    int monsterCaught = 0;
    boolean isOver = false;
    gameMap.addHunter(this);
    while (!isOver && !gameMap.getMonsters().isEmpty() && timePassed < TIME_REMAINING) {
        showMap();
        Random random = new Random();
        int randomTime = random.nextInt(1000) + 1;
        gameMap.moveHunter(this);
        long endTime = System.currentTimeMillis();
        timePassed = (endTime - initialTime);
        
        if (gameMap.getMonsters().isEmpty()) {
            isOver = true;
        }
        for (Monster monster : gameMap.getMonsters()) {
            if (monster.getPosition().equals(this.getPosition()) && !monster.isHunted()) {
                monster.setHunted(true);
                System.out.println(this.getHunterName() + " caught " + monster.getMonsterName());
                gameMap.removeMonster(monster);
                gameMap.getMonsters().remove(monster);
                monsterCaught++;
                break;
            }
        }
        try {
            Thread.sleep(randomTime);
        } catch (InterruptedException e) {
            System.out.println(hunterName + " interrupted");
        }
    }
    if (timePassed >= TIME_REMAINING || gameMap.getMonsters().isEmpty()){
        System.out.println(hunterName + " caught " + monsterCaught + " monsters");
    }
}
```

El TIME_REMAINING es una constante que determina el tiempo de la caza. También cabe mencionar que los cazadores se mueven en intervalos aleatorios, con un tiempo random en el sleep.

</div>
