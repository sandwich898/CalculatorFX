package programm.sandwich.calculater;

public class Calculate {

    public static String start(String invoice) {
        StringBuilder newIn = new StringBuilder();
        boolean stopCalculate;
        char[] inArray;

        invoice = invoice.replaceAll(" ", "");
        if (invoice.endsWith("="))
            invoice = invoice.replaceAll("=", "");

        System.out.println("Input Invoice: " + invoice);

        if(invoice.matches("[0-9+\\-*/().]+") && isInputAInvoice(invoice)) {

            while (containsAOperator(invoice) && containsNotASingleNumber(invoice)) {
                invoice = calculate(invoice).toString();
            }

            System.out.println("Ergebnis: " + invoice);
            return invoice;
        } else
            return "ERROR";

    }

    private static StringBuilder calculate(String invoice) {
        boolean stopCalculate = false;
        StringBuilder newIn = new StringBuilder();
        char[] inArray = invoice.toCharArray();


        if (invoice.contains("/")) {
            for (int i = 0; i < inArray.length; i++) {
                switch (inArray[i]) {
                    case '/' -> {
                        newIn.append(doubleToString(Double.parseDouble(getNumber1(i, inArray).toString()) / Double.parseDouble(getNumber2(i, inArray).toString())));
                        int nextOperator = getNextOperatorPostion(i, inArray);

                        if(nextOperator != -1) {
                            newIn.append(inArray[nextOperator]);
                            i = nextOperator;
                        } else {
                            i = inArray.length;
                        }

                    }
                    case '+', '-', '*' -> {
                        if (i != 0) {
                            newIn.append(getNumber1(i, inArray));
                            newIn.append(inArray[i]);
                        }
                    }
                    default -> {
                        if (getNextOperatorPostion(i, inArray) == -1)
                            newIn.append(inArray[i]);
                    }
                }
            }
        } else if (invoice.contains("*")) {
            for (int i = 0; i < inArray.length; i++) {
                switch (inArray[i]) {
                    case '*' -> {
                        newIn.append(doubleToString(Double.parseDouble(getNumber1(i, inArray).toString()) * Double.parseDouble(getNumber2(i, inArray).toString())));
                        int nextOperator = getNextOperatorPostion(i, inArray);

                        if(nextOperator != -1) {
                            newIn.append(inArray[nextOperator]);
                            i = nextOperator;
                        } else {
                            i = inArray.length;
                        }

                    }
                    case '+', '-', '/' -> {
                        if (i != 0) {
                            newIn.append(getNumber1(i, inArray));
                            newIn.append(inArray[i]);
                        }
                    }
                    default -> {
                        if (getNextOperatorPostion(i, inArray) == -1)
                            newIn.append(inArray[i]);
                    }
                }
            }
        } else if (invoice.contains("+") || invoice.contains("-")) {
            for (int i = 0; i < inArray.length; i++) {
                if(inArray[i] == '+' && !stopCalculate) {
                    newIn.append(doubleToString(Double.parseDouble(getNumber1(i, inArray).toString()) + Double.parseDouble(getNumber2(i, inArray).toString())));
                    int nextOperator = getNextOperatorPostion(i, inArray);

                    if(nextOperator != -1)
                        i = nextOperator;
                    else
                        break;

                    stopCalculate = true;
                } else if(inArray[i] == '-' && !stopCalculate && i != 0) {
                    newIn.append(doubleToString(Double.parseDouble(getNumber1(i, inArray).toString()) - Double.parseDouble(getNumber2(i, inArray).toString())));
                    int nextOperator = getNextOperatorPostion(i, inArray);

                    if(nextOperator != -1)
                        i = nextOperator;
                    else
                        break;

                    stopCalculate = true;
                }
                if(stopCalculate) {
                    newIn.append(inArray[i]);
                }
            }
        }

        System.out.println("Invoice in Progress: " + newIn.toString());
        return newIn;
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

    private static int getNextOperatorPostion(int i, char[] invoiceArray) {
        int nextOperatorPostion = -1;
        for (int j = i + 1; j < invoiceArray.length; j++) {
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

    private static boolean containsNotASingleNumber(String s) {
        if (s.contains("*") || s.contains("/") || s.contains("+"))
            return true;

        if (s.contains("-")) {
            if(s.startsWith("-")) {
                int i = 0;
                for (char c: s.toCharArray()) {
                    if (c == '-')
                        i++;
                    if (i > 1)
                        return true;
                }
                return false;
            } else
                return true;
        }

        return false;
    }
    private static boolean isInputAInvoice(String s) {
        if (containsAOperator(s) && containsNotASingleNumber(s) && !s.endsWith("+") && !s.endsWith("-") && !s.endsWith("*") && !s.endsWith("/")) {
            char[] ia = s.toCharArray();
            for (int i = 0; i < ia.length - 1; i++) {
                if (containsAOperator(ia[i] + "")) {
                    if (containsAOperator(ia[i + 1] + ""))
                        return false;
                } else if (ia[i] == '(') {
                    for (int j = i; j < ia.length - 1; j++) {
                        if (ia[j] == ')') {
                            return ia[j - 1] != '(' && !containsAOperator(ia[j - 1] + "");
                        }
                    }
                    return false;
                }
            }
            return true;
        } else return false;
    }
    private static String doubleToString(double d) {
        if (d % 1 != 0)
            return d + "";
        else return (int)d + "";
    }

}

/*
Todo:

ACHTUNG WICHTIGES PROBLEM: Im moment wird von rechts nach links gerechnet, jedoch ist das FALSCH. Siehe beispiel im Textdokument. Es muss von links nach Rechts gerechnet werden

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