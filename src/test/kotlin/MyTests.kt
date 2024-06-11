
import org.example.Calculator
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream
import kotlin.test.Test
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class MyTests {

    val inputManager = Calculator()


    @ParameterizedTest
    @MethodSource("testInputSource")
    fun testInput(string: String, expectation: String){
        val ans = inputManager.handleCmd(string)
        assertEquals(expectation, ans)
    }



    /*
    @ParameterizedTest
    @MethodSource("testInputSource2")
    fun testCalc(string: String, expectation: String){
        val ans = calculator.calculate(string).toString()
        assertEquals(expectation, ans)
    }

     */


    @ParameterizedTest
    @MethodSource("testInputSource1")
    fun testExpression(string: String, expectation: String){
        val ans = inputManager.handleExp(string)
        assertEquals(expectation, ans)
    }

    /*
    @ParameterizedTest
    @MethodSource("testInputSource2")
    fun testCreateStack(string: String, expectation: String){
        val ans = inputManager.(string)
        assertEquals(expectation, ans)
    }
    */


    fun testInputSource(): Stream<Arguments>{
        return Stream.of(
            Arguments.of("/help", "/help"),
            Arguments.of("/exit", "/exit"),
            Arguments.of("/", "bad command")
        )
    }


    fun testInputSource1(): Stream<Arguments>{
        return Stream.of(
            Arguments.of("34", "34"),
            Arguments.of("6 - 5 + 4 + 34", "6-5+4+34"),
            Arguments.of("6-5+4+34", "6-5+4+34"),
            Arguments.of("6756 78678687", "invalid expression"),
            Arguments.of("67 + 56 7867 - 8687", "invalid expression"),
            Arguments.of("--8687", "invalid expression"),
            Arguments.of("-8687", "-8687"),
            Arguments.of("868-", "invalid expression"),
            Arguments.of("868--", "invalid expression"),
            Arguments.of("+87", "+87"),
            Arguments.of("++87", "invalid expression"),
            Arguments.of("67++", "invalid expression"),
            Arguments.of("a2a","invalid identifier"),
            Arguments.of("n22","invalid identifier"),
            Arguments.of("b = c","unknown variable"),
            Arguments.of("e","unknown variable"),
            Arguments.of("a1 = 8","invalid identifier"),
            Arguments.of("n1 = a2a","invalid identifier"),
            Arguments.of("n = a2a","invalid assignment"),
            Arguments.of("= a2a","invalid identifier"),
            Arguments.of("n =","invalid assignment"),
            Arguments.of("a = 7 = 8","invalid assignment"),
        )
    }


    fun testInputSource2(): Stream<Arguments>{
        return Stream.of(
            Arguments.of("9 - 5 + ( 5 + 4 ) + (5 - 7)", "11.0"),
            Arguments.of("9 - 5 + ( 5 + 4 ) + (5 - 9 + ( 6 + 4 ) - 17)", "11.0"),
            Arguments.of("9 - 5 + ( 5 + 4 ) + ((5 - 7) + 8)", "20.0"),
            Arguments.of("9 - 5 + ( 5 + 4 - (5 - 7) )", "15.0"),
            Arguments.of("9 - 5 + (5+4)", "13.0"),
            Arguments.of("9 - 5 +++++ ( 5 + 4 )", "13.0"),
            Arguments.of("9 ____- 5 + ( 5 + 4 )", "13.0"),
            Arguments.of("9 --- 5 + ( 5 + 4 )", "13.0"),
            Arguments.of("9 - 5 + ( 5 + 4 )", "13.0"),
            Arguments.of("-( 5 + 4 )", "1.0"),
            Arguments.of("- ( 5 + 4 )", "1.0"),
            Arguments.of("-(5 + 4 )", "1.0"),
            Arguments.of("-5 + -4", "-9.0"),
            Arguments.of("6 + -4", "2.0")
        )
    }
}