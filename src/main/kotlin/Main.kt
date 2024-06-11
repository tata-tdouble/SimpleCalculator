package org.example


val inputManager = Calculator()

fun main() {
    var bool = true
    while (bool) {


        val input = inputManager.readInput()
        when {
            input == "" -> {

            }

            input == "/exit" -> {
                println("Bye!")
                bool = false
            }

            input == "/help" -> {
                println("The program calculates the sum of numbers")
            }

            input == "invalid expression" -> {
                println("Invalid expression")
            }

            input == "bad command" -> {
                println("Unknown command")
            }


            input == "unknown variable" -> {
                println("Unknown variable")
            }


            input == "invalid identifier" -> {
                println("Invalid identifier")
            }

            input == "invalid assignment" -> {
                println("Invalid assignment")
            }

            Regex("^[+-]\\d+$").matches(input) -> {
                println(input)
            }

            else -> {
                val code = inputManager.getCoder()
                val extractor = inputManager.getExtractor()
                val varExtract = inputManager.getVarExtractor()
                val calculatorItem = inputManager.createCalculatorItem(input, code, extractor, varExtract)
                val ans = calculatorItem.calculate()
                println(ans)
                //println(list)
                //println(sList)

            }
        }
    }
}