package org.example

class CalculatorItem(
    val expression: String,
    val parent : CalculatorItem?,
    val code : String.() -> (String),
    val extract : (String, String) -> List<String>,
    val varExtract : (String) -> String
) {

    private var children: MutableList<CalculatorItem?>

    init {
        children = createChildren()
    }

    fun createChildren() : MutableList<CalculatorItem?> {
        val list =  mutableListOf<CalculatorItem?>()
        val coded = expression.code()
        val children = extract(expression, coded)
        for (i in children){
            list.add(CalculatorItem(i, this, code, extract, varExtract))
        }

        return list
    }


    fun calculate() : Double{

        //println("express $expression")
        val lVar = expression.code()
            .split(Regex("""[-+/*()]"""))
            .filter { it != ""  }
        val oVar = expression.code()
            .split(Regex("""[^-+/*()]"""))
            .filter { it != ""  }
            .map { it[0] }
        //println(lVar)
        //println(oVar)
        val cResult: MutableList<Double?> = mutableListOf()
        var n = 0
        for (i in lVar.indices) {
            if(Regex("@+").matches(lVar[i])){
                cResult.add(children[n]?.calculate())
                n++
            } else {
                cResult.add(lVar[i].toDoubleOrNull())
            }
        }

        return doBodmas(cResult, oVar.toMutableList())
    }

    fun doBodmas(input: MutableList<Double?>, opp: MutableList<Char>): Double {
        val order = listOf('/','*','-','+')
        for (i in order){
            var count = 0
            while(count < opp.size){
                if (opp[count] == i){
                    input[count] = doMath(input[count]!!, input[count+1]!!, opp[count])
                    input.removeAt(count+1)
                    opp.removeAt(count)
                    if (count != 0) count--
                } else {
                    count++
                }
            }
        }

        return input[0] ?: 0.0
    }

    fun doMath(a: Double, b: Double, opp: Char): Double {
        return when(opp){
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> a / b
            else -> a + b
        }
    }

}