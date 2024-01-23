package programm.sandwich.calculater;

import java.util.regex.Pattern;

public class Calculate {

    public static String start(String invoice) {
        StringBuilder newIn = new StringBuilder();
        boolean lastTimeCalculate;
        char[] inArray;

        invoice = invoice.replaceAll(" ", "");

        System.out.println("Input Invoice: " + invoice);

        if(invoice.matches("[0-9+\\-*/().]+")) {

            while (containsAOperator(invoice) && !containsASingleNumber(invoice)) {
                lastTimeCalculate = false;
                newIn.delete(0, newIn.length());
                inArray = invoice.toCharArray();

                if (invoice.contains("/")) {
                    for (int i = inArray.length - 1; i > -1; i--) {
                        switch (inArray[i]) {
                            case '/' -> {
                                newIn.insert(0, doubleToString(Double.parseDouble(getNumber1(i, inArray).toString()) / Double.parseDouble(getNumber2(i, inArray).toString())));

                                int nextOperator = getNextOperatorPostion(i, inArray);
                                if(nextOperator != -1)
                                    newIn.insert(0, inArray[nextOperator]);

                                lastTimeCalculate = true;
                            }
                            case '+', '-', '*' -> {
                                if(!lastTimeCalculate) {
                                    newIn.insert(0, getNumber2(i , inArray));
                                    newIn.insert(0, inArray[i]);
                                } else
                                    lastTimeCalculate = false;
                            }
                        }
                        if (!lastTimeCalculate && i == 0)
                            newIn.insert(0, getNumber2(i , inArray));

                    }
                } else if (invoice.contains("*")) {
                    for (int i = inArray.length - 1; i > -1; i--) {
                        switch (inArray[i]) {
                            case '*' -> {
                                newIn.insert(0, doubleToString(Double.parseDouble(getNumber1(i, inArray).toString()) * Double.parseDouble(getNumber2(i, inArray).toString())));

                                int nextOperator = getNextOperatorPostion(i, inArray);
                                if(nextOperator != -1)
                                    newIn.insert(0, inArray[nextOperator]);

                                lastTimeCalculate = true;
                            }
                            case '+', '-', '/' -> {
                                if(!lastTimeCalculate) {
                                    newIn.insert(0, getNumber2(i , inArray));
                                    newIn.insert(0, inArray[i]);
                                } else
                                    lastTimeCalculate = false;
                            }
                        }
                        if (!lastTimeCalculate && i == 0)
                            newIn.insert(0, getNumber2(i , inArray));

                    }
                } else if (invoice.contains("+") || invoice.contains("-")) {
                    for (int i = inArray.length - 1; i > -1; i--) {
                        switch (inArray[i]) {
                            case '+' -> {

                                double result = Double.parseDouble(getNumber1(i, inArray).toString()) + Double.parseDouble(getNumber2(i, inArray).toString());
                                int nextOperator = getNextOperatorPostion(i, inArray);

                                if(result < 0) {
                                    if (inArray[nextOperator] == '-') {
                                        result = Math.abs(result);
                                        nextOperator = -2;
                                    } else if (inArray[nextOperator] == '+') {
                                        nextOperator = -1;
                                    }
                                }

                                newIn.insert(0, doubleToString(result));

                                if (nextOperator == -2)
                                    newIn.insert(0, "+");
                                else if (nextOperator != -1)
                                    newIn.insert(0, inArray[nextOperator]);

                                i = getNextOperatorPostion(i, inArray) - 1;
                            }
                            case '-' -> {
                                if(i != 0) {

                                    double result = Double.parseDouble(getNumber1(i, inArray).toString()) - Double.parseDouble(getNumber2(i, inArray).toString());
                                    int nextOperator = getNextOperatorPostion(i, inArray);

                                    if(result < 0 && nextOperator != -1) {
                                        if (inArray[nextOperator] == '-') {
                                            result = -(result);
                                            nextOperator = -2;
                                        } else if (inArray[nextOperator] == '+') {
                                            nextOperator = -1;
                                        }
                                    }

                                    newIn.insert(0, doubleToString(result));

                                    if (nextOperator == -2)
                                        newIn.insert(0, "+");
                                    else if (nextOperator != -1)
                                        newIn.insert(0, inArray[nextOperator]);

                                    i = getNextOperatorPostion(i, inArray) - 1;
                                }
                            }
                        }

                        if (i == 0)
                            newIn.insert(0, getFirstNumber(i , inArray));
                    }
                }
                System.out.println("Invoice in Progress: " + newIn.toString());
                invoice = newIn.toString();
            }

            System.out.println("Ergebnis: " + invoice);
            return invoice;
        } else
            return "ERROR";

    }

    private static StringBuilder getNumber1(int i, char[] invoiceArray) {
        StringBuilder number1 = new StringBuilder();
        for (int j = i - 1; j >= 0; j--) {
            if (containsAOperator(invoiceArray[j] + "")) {
                if(invoiceArray[j] == '-' && j == 0)
                    number1.append("-");
                break;
            } else
                number1.append(invoiceArray[j]);
        }
        return number1.reverse();
    }

    private static StringBuilder getNumber2(int i, char[] invoiceArray) {
        StringBuilder number2 = new StringBuilder();
        for (int j = i + 1; j < invoiceArray.length; j++) {
            if (containsAOperator(invoiceArray[j] + ""))
                break;
            else
                number2.append(invoiceArray[j]);
        }
        return number2;
    }

    private static StringBuilder getFirstNumber(int i, char[] invoiceArray) {
        StringBuilder number = new StringBuilder();
        for (int j = i; j < invoiceArray.length; j++) {
            if (containsAOperator(invoiceArray[j] + ""))
                break;
            else
                number.append(invoiceArray[j]);
        }
        return number;
    }

    private static int getNextOperatorPostion(int i, char[] invoiceArray) {
        int nextOperatorPostion = -1;
        for (int j = i - 1; j >= 0; j--) {
            if (containsAOperator(invoiceArray[j] + "") && j != 0) {
                nextOperatorPostion = j;
                break;
            }
        }
        return nextOperatorPostion;
    }

    private static boolean containsAOperator(String s) {
        return (s.contains("*") || s.contains("/") || s.contains("+") || s.contains("-"));
    }

    private static boolean containsASingleNumber(String s) {
        if (s.contains("*") || s.contains("/") || s.contains("+"))
            return false;

        if (s.contains("-")) {
            if(s.startsWith("-")) {
                int i = 0;
                for (char c: s.toCharArray()) {
                    if (c == '-')
                        i++;
                    if (i > 1)
                        return false;
                }
                return true;
            } else
                return false;
        }

        return true;
    }
    private static String doubleToString(double d) {
        if (d % 1 != 0)
            return d + "";
        else return (int)d + "";
    }

}

/*
Todo:

ACHTUNG WICHTIGES PROBLEM: Im moment wird von rechts nach links gerechnet, jedoch ist das FALSCH. Sihe beispiel im Textdokument. Es muss von links nach Rechts gerechnet werden

#. Gleichzeichen automatisch am ende Entfernen damit kein Error entsteht.
#. Überprüfung auf nicht autorisierte Zeichen ein fügen.
3. + und - rechnung einfügen (Bei Mal und Geteilt falls man eine Rechung schreibt wo am anfang ein Minus Steht (Zumbeispiel -2*3))
#. Automatische Leerzeichen entfernung einfügen.
5. Code besser getallten (Methoden hinzufügen)

1. Komme rechnung
2. Klammer rechnung
	-Drauf achten das wenn vor der Klammer eine Zahl steht ohne Operator daneben, wird die klamer mit der Zahl Mal gerechent
	-Klamern werden als neuen Invoice gebaut und extra ausgerchenet, wenn dan noch eine Klamer drin ist wieder eine Neue rechnung. Das ergebnis wird dann einfach für die ganze Klamer ersetzt
	-Überprüfen ob es Klammern gibt, ob die erste Klammer eine Klammer auf ist und ob es danch eine Klammer zu exestirt
	-Überprüfen ob in der Klammer auch eine Rechnung steht, nicht nur eine Zahl
3. Minus rechung
 */