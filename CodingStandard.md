codificationStandard.md

# Estándar de codificación

En éste archivo se pueden ver los estándares de codificación que **Olimpo** usará para programar en los siguientes lenguajes:
 - [Todos los lenguajes](#general)
     - [Formato y espaciado](#formato-y-espaciado)
     - [Llaves](#llaves)
     - [Comentarios](#comentarios)
     - [Nombres e identificadores](#nombres-e-identificadores)
     - [Declaraciones de control](#declaraciones-de-control)
 - [Kotlin](#exclusivos-kotlin)
     - [Formato](#formato)
     - [Clases](#clases)
     - [Variables](#variables)
     - [Funciones](#funciones)
     - [Documentación](#documentación)

Éstos estándares están basados de manera parcial en:
 - [Estándar de Google para Kotlin](https://github.com/airbnb/swift)

## General:
 > [!NOTE]
 > En este apartado se usará JavaScript como ejemplo en los bloques de código

 - [Formato y espaciado](#formato-y-espaciado)
 - [Llaves](#llaves)
 - [Comentarios](#comentarios)
 - [Nombres e identificadores](#nombres-e-identificadores)
 - [Declaraciones de control](#declaraciones-de-control)

### Formato y espaciado:
 - Utilizar 4 espacios por cada tabulación.
    ```js
    // mal
    if (isTrue()) {
    if (isTrue()) {
        if (isTrue()) {
            // ...
        }
    }
    }

    // bien
    if (isTrue()) {
        if (isTrue()) {
            if (isTrue()) {
                // ...
            }
        }
    }
    ```

 - Al asignarle un valor a una variable o usar operadores en general, separar con espacios cada valor y operador.
    ```js
    // mal
    let sum=10+20+30

    // mal
    let sum = 10+20+30

    // bien
    let sum = 10 + 20 + 30
    ```

 - Dejar una línea en blanco al término de cada bloque.
    ```js
    // mal
    if (isTrue()) {
        // ...
    }
    if (isFalse()) {
        // ...
    }

    // bien
    if (isTrue()) {
        // ...
    }

    if (isFalse()) {
        // ...
    }
    ```

 - Si al declarar una función se ocupan muchos parámetros y se vuelve imposible que todo esté en una sola línea, declarar cada parámetro en su propia línea.
    ```js
    // mal
    function processData(let variable1, let variable2, let variable3, let variable4, let variable5, let variable6) {
        // ...
    }

    // bien
    function processData(
        let variable1,
        let variable2,
        let variable3,
        let variable4,
        let variable5,
        let variable6
    ) {
        // ...
    }
    ```

 - Si el lenguaje no lo necesita, no usar `;`.


### Llaves:
 - Para expresiones if y when, siempre usar llaves, aunque solo tengan una línea (a excepción que sea un operador ternario):
    ```js
    // mal
    if (value > 10) value = 0

    // bien
    if (value > 10) {
        value = 0
    }
    ```


 - Seguir el estilo de llaves de _Kernighan y Ritchie_ para bloques con llaves (a excepción que sea un operador ternario).
     - Sin salto de línea antes de la llave de apertura.
     - Salto de línea después de la llave de apertura.
     - Salto de línea antes de la llave de cierre.
     - Salto de línea después de la llave de cierre (solo si esa llave termina, en caso contrario un espacio).

    ```js
    // mal
    if (value > 10)
    {
        // ...
    }
    else
    {
        // ...
    }

    // bien
    if (value > 10) {
        // ...
    } else {
        // ...
    }
    ```


### Declaraciones de control:
 - Separar los paréntesis de la palabra reservada y las llaves con espacio.
    ```js
    // mal
    if(isTrue()){
        // ...
    }

    // bien
    if (isTrue()) {
        // ...
    }
    ```

 - Separar cada condición con espacios.
    ```js
    // mal
    if (isTrue()&&isTrueAgain()) {
        // ...
    }

    // bien
    if (isTrue() && isTrueAgain()) {
        // ...
    }
    ```

 - Si tus declaraciones de control terminan siendo muy largas, separar en varias líneas, y asegurar de que inicien con su operador lógico; e iniciar las declaraciones en una línea extra.
    ```js
    // mal
    if ( firstCond() && secondCond() && thirdCond() ) {
        // ...
    }

    // mal
    if ( 
        firstCond() && 
        secondCond() && 
        thirdCond() 
    ) {
        // ...
    }

    // mal
    if ( firstCond() 
        && secondCond() 
        && thirdCond() ) {
        // ...
    }

    // bien
    if ( 
        firstCond() 
        && secondCond() 
        && thirdCond() 
    ) {
        // ...
    }
    ```


### Comentarios:
 - Si el comentario es de varias líneas, usar el comentario multilinea de dicho lenguaje.
    ```js
    // mal
    // Voy a hacer varios comentarios
    // Comentario 1
    // Comentario 2

    // bien
    /**
     * Voy a hacer varios comentarios
     * Comentario 1
     * Comentario 2
    */
    ```

 - Iniciar cada comentario con un espacio para que sea fácil de leer.
    ```js
    // mal
    //esto es mi comentario

    // bien
    // esto es mi comentario
    ```

 - Para comentarios de una sola línea poner una línea vacía antes del comentario a menos de que sea la primer línea del bloque, o que se estén encadenando funciones, en cuyo caso sí se puede comentar sobre la misma línea.
    ```js
    // mal
    if (isTrue) {

        // Significa que es verdadero
        const zero = 0
        // Le pongo un valor
        const one = 1
    }

    // mal
    if (isTrue) {

        // Significa que es verdadero
        const zero = 0
        
        // Le pongo un valor
        const one = 1
    }

    // bien
    if (isTrue) {
        // Significa que es verdadero
        const zero = 0

        // Le pongo un valor
        const one = 1
    }

    // bien
    funcionA // Comentario sobre la línea
    .funcionB // Comentario sobre la línea
    .funcionC // Comentario sobre la línea
    ```

### Nombres e identificadores:
 - Usar `SCREAMING_SNAKE_CASE` para nombrar constantes.
    ```js
    // mal
    const pi = 3.14
    const ourName = "Olimpo"

    // bien
    const PI = 3.14
    const OUR_NAME = "Olimpo"
    ```

 - Usar `PascalCase` para nombrar clases, estructuras, interfaces, protocolos, enum o tipos.
    ```js
    // mal
    class mountOlympus {
        // ...
    }

    // bien
    class MountOlympus {
        // ...
    }
    ```

 - Usar `camelCase` para nombrar métodos y variables.
    ```js
    // mal
    let FreeDay = "Wednesay"

    // bien
    let freeDay = "Wednesday"
    ```

 - Ser descriptivo con los nombres que hagas.
    ```js
    // mal
    let x = 5

    // bien
    let teamAmount = 5
    ```

 - Si usas un booleano, asegegurar de incluir una palabra como `is` o `has` para denotar que es un predicado.
    ```js
    // mal
    let active = true

    // bien
    let isActive = true
    ```

 - En lenguajes tipados, siempre especificar de forma explícita el tipo de cada variable.
    ```kotlin
    // Ejemplo en Kotlin

    // mal
    var ourName = "Olimpo"

    // bien
    val ourName: String = "Olimpo"
    ```


### Strings:
 - Usar comillas dobles `" "`, salvo de que la situación no lo permita.
    ```js
    // mal
    const ourName = 'Olimpo'

    // bien
    const ourName = "Olimpo"
    ```

 - Usar string multilinea solo en caso de que sea estrictamente necesario.
    ```js
    // mal (string corto innecesariamente en multilínea)
    const greeting = `
    Hola
    Olimpo
    `;

    // mal (string de una sola línea)
    const message = `Nothing.`

    // bien (string extenso donde mejora la legibilidad)
    const errorMessage = `
    Error: No se pudo conectar con el servidor.
    Posibles causas:
    - La red no está disponible.
    - El servidor no responde.
    - La configuración es incorrecta.
    `;
    ```

## Exclusivos Kotlin:
 - [Formato](#formato)
 - [Clases](#clases)
 - [Variables](#variables)
 - [Funciones](#funciones)
 - [Documentación](#documentación)

### Formato:
 - Seguir el siguiente orden para cada archivo:
    1. Anotaciones a nivel de archivo.
    1. Declaración de paquete.
    1. Declaraciones de importación.
    1. Declaraciones de nivel superior.

### Clases:
 - Declarar los enum classes en una línea si no tienen cuerpo, o en varias si tienen cuerpo o funciones.

    ```kotlin
    // mal
    enum class Answer {
        YES, NO, MAYBE
    }

    // bien (sin cuerpo)
    enum class Answer { YES, NO, MAYBE }

    // bien (con cuerpo)
    enum class Answer {
        YES,
        NO,
        MAYBE {
            override fun toString() = """¯\_(ツ)_/¯"""
        }
    }
    ```

### Variables:
 - Si la variable es privada, usar el prefijo `_` para acceder a ella.
    ```kotlin
    private var _table: Map<String, Int>? = null

    val table: Map<String, Int>
        get() {
            if (_table == null) {
                _table = HashMap()
            }
            return _table ?: throw AssertionError()
        }
    ```

 - Si se ocupa usar un tipo genérico, usar `T`, `E` o `R`, o combinaciones de tipo `FooT`.
    ```kotlin
    class Box<T>(val value: T)

    fun <E> List<E>.firstOrNull(): E? = if (isEmpty()) null else this[0]
    ```

### Funciones:
 - Poner los nombres de las funciones en `CamelCase`, exceptuando las que sean `@Composable`, las cuales van en `PascalCase`

    ```kotlin
    // mal
    fun TestEveryPossibleCase() {
        // ...
    }

    // mal
    @Composable
    fun nameTag(name: String) {
        // ...
    }

    // bien
    fun testEveryPossibleCase() {
        // ...
    }

    // bien
    @Composable
    fun NameTag(name: String) {
        // ...
    }
    ```

### Documentación:
 - Al documentar una clase o función, escribir el primer párrafo de forma breve y descriptiva.
    ```kotlin
    /**
     * Calcula la edad promedio de los usuarios.
     *
     * @param ages Lista de edades.
     * @return Edad promedio.
     */
    fun averageAge(ages: List<Int>): Double {
        // ...
    }
    ```

 - Usar las etiquetas en el bloque en este orden: `@constructor`, `@receiver`, `@param`, `@property`, `@return`, `@thros`, `@see`.