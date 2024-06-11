package org.example

class Calculator {

    val map = mutableMapOf<String, String>()

    fun readInput(): String{
        val input = readln().trim()
        if (input=="") return ""
        when(input[0]) {
            '/' -> return handleCmd(input)
            else -> return handleExp(input)
        }
    }

    fun handleExp(str: String): String{

        when{
            !isBalanced(str) -> return "invalid expression"
            Regex("/{2,}").containsMatchIn(str) -> return "invalid expression"
            Regex("\\d+\\s+\\d+").containsMatchIn(str) -> return "invalid expression"
            Regex("^[-+]{2,}\\d+$").matches(str) -> return "invalid expression"
            Regex("^[-+]{2,}\\d+.*").matches(str) -> return "invalid expression"
            Regex("\\*{2,}").containsMatchIn(str) -> return "invalid expression"
            Regex("^\\d+[+-]+$").matches(str) -> return "invalid expression"
            Regex(".*\\d+[+-]+$").matches(str) -> return "invalid expression"
            Regex("[a-zA-Z]+|.*=.*").containsMatchIn(str) -> return handleAss(sanitizeInput(str))
            Regex("^\\s*\\d+\\s*$").matches(str) -> return sanitizeInput(str)
            else -> return sanitizeInput(str)
        }
    }

    fun handleAss(str: String): String{
        if(!isValidVariableName(str)) {
            val list = str.split("=").map { it.trim() }
            when {
                list.size == 0 -> return "invalid identifier"
                list.size == 1 -> return handleIdentidfier(list[0])
                list.size == 2 -> return handleAssignment(list)
                else -> return "invalid assignment"
            }
        } else {
            return getVariable(str)
        }
    }

    fun handleIdentidfier(str: String): String{
        when{
            checkVariable(str) -> return  getVariable(str)
            else -> return varExtractor(str)
        }
    }

    fun isValidVariableName(str: String): Boolean {
        return Regex("^[a-zA-Z]+$").matches(str)
    }

    fun isValidDigit(str: String): Boolean {
        return Regex("^[-+]*\\d+\\b").matches(str)
    }

    fun handleAssignment(list: List<String>): String {
        if (isValidVariableName(list[0])) {
            when {
                isValidVariableName(list[1]) -> {
                    val it = getVariable(list[1])
                    return if (it != "unknown variable") setVariable(list[0], it) else "unknown variable"
                }
                isValidDigit(list[1]) -> return setVariable(list[0], list[1])// todo
                else -> return "invalid assignment"
            }
        } else {
            return "invalid identifier"
        }
    }

    fun setVariable(str: String, value: String) : String {
        map[str] = value
        //println(map)
        return ""
    }

    fun checkVariable(str: String) : Boolean {
        return map.containsKey(str)
    }

    fun getVariable(str: String): String{
        //println(map)
        return if (map.containsKey(str)) map.getOrDefault(str, "") else "unknown variable"
    }

    fun handleCmd(str: String): String{
        //println("input 2 $str")
        when{
            str == "/help" -> return "/help"
            str == "/exit" -> return "/exit"
            else -> return "bad command"
        }
    }

    fun sanitizeInput(string: String): String{
        val str = string.split(" ").map {
            arrangeString(it)
        }.joinToString("")
        return arrangeParenthesis(str)

    }

    fun arrangeParenthesis(str: String): String{
        var count = 0
        val list = mutableListOf<Char>()
        for (ch in str ) {
            if (ch == '(')
                count++
            if (ch == ')')
                count--
            if (count < 0)
                break
            list.add(ch)
        }
        return list.joinToString("")
    }

    fun arrangeString(str: String): String {


        val str1 = handleOddAndEvenNegatives(str)

        val regex2 = "\\++".toRegex()
        val str2 = regex2.replace(str1, "+")

        val regex3 = "\\*+".toRegex()
        val str3 = regex3.replace(str2, "*")

        val regex4 = "/+".toRegex()
        val str4 = regex4.replace(str3, "/")

        //val regex5 = "-\\d+".toRegex()
        return str4
    }

    fun handleOddAndEvenNegatives(str: String): String {

        var st = str
        val regex1 = "-+".toRegex()
        val strs = regex1.findAll(str).toList().map {
            it.value
        }
        for(i in strs){
            if(i.length % 2 == 0 ){
                st = regex1.replaceFirst(str, "+")
            } else {
                st = regex1.replaceFirst(str, "-")
            }
        }
        return st
    }

    fun createCalculatorItem(str: String, code : String.() -> (String), extract : (String, String) -> List<String>, varExtract : (String) -> String ): CalculatorItem {
        return CalculatorItem(str, null, code, extract, varExtract)
    }

    fun getCoder() : String.() -> String {
        return {
            val str = this
            val output = buildString {
                var count = 0
                for (ch in str) {
                    when {
                        ch == '(' -> {
                            count++
                            append('@')
                        }

                        ch == ')' -> {
                            count--
                            append('@')
                        }

                        count == 0 -> {
                            append(ch)
                        }

                        count != 0 -> {
                            append('@')
                        }
                    }

                }
            }
            Regex("\\+-").replace(output, "-")
        }
    }

    fun isBalanced(str: String) : Boolean {
        var openCount = 0
        for (ch in str){
            when (ch) {
                '(' -> openCount++
                ')' -> {
                    if (openCount == 0) return false
                    openCount--
                }
            }
        }
        return openCount == 0
    }

    fun getExtractor() : (String, String) -> List<String> {
        return ::extract
    }

    fun getVarExtractor() : (String) -> String {
        return ::varExtractor
    }

    fun extract (str1: String, str2: String)  : List<String> {

        val sb = StringBuilder()
        val missingParts = mutableListOf<String>()
        var i = 0
        while (i < str1.length && i < str2.length) {
            if (str1[i] == str2[i]) {
                i++
                if (sb.isNotEmpty()) {
                    missingParts.add(sb.toString())
                    sb.clear()
                } else {
                    continue
                }
            }
            if (str2[i] == '@') {
                sb.append(str1[i])
            }
            i++
        }

        if (sb.isNotEmpty()) {
            missingParts.add(sb.toString())
            sb.clear()
        }

        return missingParts.toList().map{
            var str : String = it
            while (str[0] == '(' && str[str.lastIndex] == ')') {
                str = str.substring(1, str.lastIndex)
            }
            str
        }

    }

    fun varExtractor (str: String)  : String {
        var ans = str
        val regex = Regex("[a-zA-Z]+")
        var match = regex.find(str)
        while (match != null){
            println("match.value")
            println(match.value)
            ans = ans.replace(match.value, getVariable(match.value))
            match = match.next()
        }
        println("ans $ans")
        return ans
    }

}
