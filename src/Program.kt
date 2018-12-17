import java.util.logging.Handler
import kotlin.concurrent.thread
import kotlin.random.Random

var g = Graph()
var animals = ArrayList<ArrayList<Graph.Vertex>>()
val random = Random(System.currentTimeMillis())
fun main(vararg args: String){
//    var g = Graph()

    g.addVertex("1")
    g.addVertex("2")
    g.addVertex("3")
    g.addVertex("4")
    g.addVertex("5")
    g.addVertex("6")
    g.connect("1","2", 3)
    g.connect("1","3", 4)
    g.connect("2","4", 7)
    g.connect("2","5", 4)
    g.connect("3","5", 3)
    g.connect("3","4", 5)
    g.connect("4","6", 1)
    g.connect("5","6", 9)
    for(i in 0..14){
        var animal = createAnimal(g)
        animals.add(animal)
        print("$animal\n")
    }
    print("----------------------------------------\n")
    var new = selectionTournament(animals)
    new.forEach { print("$it\n") }

}

fun selectionTournament(animals: ArrayList<ArrayList<Graph.Vertex>>): ArrayList<ArrayList<Graph.Vertex>>{
    var arr = ArrayList<ArrayList<Graph.Vertex>>()
    for(i in 0..14){
        val temp1 = animals[random.nextInt(animals.size)]
        val temp2 = animals[random.nextInt(animals.size)]

            if (calculatePath(temp1) <= calculatePath(temp2)) {
                arr.add(temp1)
            }else{
                arr.add(temp2)
            }

    }
    return arr
}

fun mutate(animals: ArrayList<ArrayList<Graph.Vertex>>): ArrayList<ArrayList<Graph.Vertex>>{
    var indexAnimal = Random.nextInt(0, animals.size-1)
    val mutant = animals[indexAnimal]
    var indexVertex : Int
    if(1 <= mutant.size-2 ){
        indexVertex = 1
    }else {
        indexVertex = Random.nextInt(1, mutant.size - 2)
    }
    val neighborLeft = mutant[indexVertex-1]
    var candidates = ArrayList<Graph.Vertex>()
    g.vertices.forEach{
        if(it.value.neighbors.contains(neighborLeft) && it != animals[1] && it != animals[animals.lastIndex]){
            candidates.add(it.value)
        }
    }
    mutant[indexVertex] = candidates[Random.nextInt(0, candidates.size-1)]
    var arr = ArrayList<Graph.Vertex>()
    for(i in 0..indexVertex){ arr.add(mutant[i]) }
    finishAnimal(arr)
    print("MUTANT $arr")
    animals[indexAnimal] = arr
    return animals
}

fun finishAnimal(arr: ArrayList<Graph.Vertex>): ArrayList<Graph.Vertex>{
    var temp1 = arr[arr.lastIndex]
    var tempArr = ArrayList<Graph.Vertex>()
    var temp2: Graph.Vertex
    var cont = true
    while(cont) {
        temp2 = g.vertices[temp1.name]!!.neighbors[random.nextInt(g.vertices[temp1.name]!!.neighbors.size)]
        if(temp2.name == "6") cont = false
        if(!tempArr.contains(temp2)  && !arr.contains(temp2)) {
            tempArr.add(temp2)
            temp1 = temp2
        }
    }
   arr.addAll(tempArr)
    return arr
}

fun createAnimal(g: Graph): ArrayList<Graph.Vertex>{
    var tempArr = ArrayList<Graph.Vertex>()
    var cont = true
    var temp1 = g.vertices["1"]!!.neighbors[random.nextInt(g.vertices["1"]!!.neighbors.size)]
    tempArr.add(g.vertices["1"]!!)
    tempArr.add(temp1)
    var temp2: Graph.Vertex
    while(cont) {
        temp2 = g.vertices[temp1.name]!!.neighbors[random.nextInt(g.vertices[temp1.name]!!.neighbors.size)]
        if(temp2.name == "6") cont = false
        if(!tempArr.contains(temp2)) {
            tempArr.add(temp2)
            temp1 = temp2
        }
    }
    return tempArr
}

fun calculatePath(path: ArrayList<Graph.Vertex>): Int{
    var weight = 0
    for(i in 0 until path.lastIndex){
        weight = g.weights["${path[i].name}-${path[i+1].name}"]?:g.weights["${path[i+1].name}-${path[i].name}"]!!
        print("${path[i].name}-${path[i+1].name}   $weight\n")
    }
    return weight
}

fun crossoverAllAnimals(animals: ArrayList<ArrayList<Graph.Vertex>>){
    var alpha = animals[0]
    animals.forEach { if(calculatePath(it)<calculatePath(alpha)) alpha = it }
    if(animals.size % 2 == 0){

    }else{

    }
}

fun crossoverTwoAnimals(alpha: ArrayList<Graph.Vertex>, notAlpha: ArrayList<Graph.Vertex>): ArrayList<Graph.Vertex>?{
    var leftCommon = Graph.Vertex("")
    var rightCommon = Graph.Vertex("")
    for(i in 1 until alpha.lastIndex){
        for(j in 1 until notAlpha.lastIndex){
            if( alpha[i] == notAlpha[j]){
                rightCommon = alpha[i]
            }
        }
    }
    for(i in alpha.lastIndex-1..1){
        for(j in notAlpha.lastIndex-1..1){
            if(alpha[i] == notAlpha[j]){
                rightCommon = alpha[i]
            }
        }
    }
    if((alpha.indexOf(leftCommon)-alpha.indexOf(rightCommon)>1 &&
                (notAlpha.indexOf(leftCommon) - notAlpha.indexOf(rightCommon))>1 &&
                leftCommon != Graph.Vertex("") && rightCommon != Graph.Vertex(""))){
        var alphaBuffer = ArrayList<Graph.Vertex>()
        for(i in alpha.indexOf(leftCommon)+1..alpha.indexOf(rightCommon)-1){
            alphaBuffer.add(alpha[i])
        }
        for(i in notAlpha.indexOf(leftCommon)+1..notAlpha.indexOf(rightCommon)-1){
            notAlpha.removeAt(i)
        }
        notAlpha.addAll(notAlpha.indexOf(leftCommon), alphaBuffer)
        return notAlpha
    }
    return null
}

class Graph {
    data class Vertex(val name: String) {
        val neighbors = arrayListOf<Vertex>()
    }
    val weights = mutableMapOf<String, Int>()

    val vertices = mutableMapOf<String, Vertex>()
    private fun connect(first: Vertex?, second: Vertex?) {
        if (second != null) first?.neighbors?.add(second)
        if (first != null) second?.neighbors?.add(first)
    }

    fun addVertex(name: String) {
        vertices[name] = Vertex(name)
    }

    fun connect(first: String, second: String, weight: Int) {
        connect(vertices[first], vertices[second])
        weights["$first-$second"] = weight
    }

    fun neighbors(name: String): List<String> =
        vertices[name]?.neighbors?.map { it.name } ?: listOf()
}