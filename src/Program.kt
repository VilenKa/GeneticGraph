import kotlin.random.Random

var g = Graph()
var animals = ArrayList<ArrayList<Graph.Vertex>>()
fun main(vararg args: String){
//    var g = Graph()

    g.addVertex("1")
    g.addVertex("2")
    g.addVertex("3")
    g.addVertex("4")
    g.addVertex("5")
    g.connect("1","2", 4)
    g.connect("1","3", 7)
    g.connect("2","4", 9)
    g.connect("3","5", 3)
    g.connect("4","5", 2)
    for(i in 0..15){
        var animal = createAnimal(g)
        animals.add(animal)
        print("$animal\n")
    }
}

fun selectionTournament(something: MutableMap<ArrayList<Graph.Vertex>, Int>): ArrayList<ArrayList<Graph.Vertex>>{
    var arr = ArrayList<ArrayList<Graph.Vertex>>()
    for(i in 0..15){
        var temp1 = animals[Random.nextInt(0,animals.size-1)]
        var temp2 = animals[Random.nextInt(0,animals.size-1)]
        if(temp1 != temp2) {
            if (calculatePath(temp1) < calculatePath(temp2)) {
                arr.add(temp1)
            }else{
                arr.add(temp2)
            }
        }
    }
    return arr
}

fun mutate(animals: ArrayList<ArrayList<Graph.Vertex>>){
    var indexAnimal = Random.nextInt(0, animals.size-1)
    val mutant = animals[indexAnimal]
    var indexVertex = Random.nextInt(1, mutant.size-2)
    val neighborLeft = mutant[indexVertex-1]
    val neighborRight = mutant[indexVertex+1]
    var candidates = ArrayList<Graph.Vertex>()
    g.vertices.forEach{
        if(it.value.neighbors.contains(neighborLeft) && it.value.neighbors.contains(neighborRight)){
            candidates.add(it.value)
        }
    }
    mutant[indexVertex] = candidates[Random.nextInt(0, candidates.size-1)]
}

fun createAnimal(g: Graph): ArrayList<Graph.Vertex>{
    var tempArr = ArrayList<Graph.Vertex>()
    var cont = true
    var temp1 = g.vertices["1"]!!.neighbors[Random.nextInt(1, g.vertices["1"]!!.neighbors.size)]
    tempArr.add(g.vertices["1"]!!)
    tempArr.add(temp1)
    var temp2: Graph.Vertex
    while(cont) {
        temp2 = g.vertices[temp1.name]!!.neighbors[Random.nextInt(1, g.vertices[temp1.name]!!.neighbors.size)]
        if(temp2.name == "4") cont = false
        if(!tempArr.contains(temp2)) tempArr.add(temp2)
        temp1 = temp2
    }
    return tempArr
}

fun calculatePath(path: ArrayList<Graph.Vertex>): Int{
    var weight = 0
    for(i in 0..path.size-1){
        weight += g.weights["$i-${i+1}"]!!
    }
    return weight
}



class Graph {
    data class Vertex(val name: String) {
        val neighbors = arrayListOf<Vertex>()
    }
    var weights = mutableMapOf<String, Int>()

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