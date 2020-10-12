package ru.mikheev.kirill.math;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Kirill Mikheev
 * @version 1.0
 */

public class Parser {
    interface ExpressionMember  {
        double getValue();
        int getPriority();
    }

    static class SubExpression implements ExpressionMember {

        private ArrayList<ExpressionMember> parts;
        private ExpressionMember secondPart;
        private Operations operation;
        private boolean newLevel = false;

        public SubExpression(ArrayList<ExpressionMember> parts, Operations operation){
            this.parts = parts;
            this.operation = operation;
        }

        public void setNewLevel(){
            newLevel = true;
        }

        public void rotateExpression(ExpressionMember newMain, Operations newOperation){
            ArrayList<ExpressionMember> newMembers = new ArrayList<>();
            newMembers.add(this.parts.get(1));
            newMembers.add(newMain);
            SubExpression tmp = new SubExpression(newMembers , newOperation);
            this.parts.set(1, tmp);
        }

        @Override
        public double getValue() {
            return operation.calculation(parts);
        }

        @Override
        public int getPriority() {
            return newLevel ? 4 : operation.getPriority();
        }
    }

    static class Token implements ExpressionMember {

        private double value;

        public Token(double value){
            this.value = value;
        }

        @Override
        public double getValue() {
            return value;
        }

        @Override
        public int getPriority() {
            return 4;
        }
    }

    static enum Operations {
        ADDITION(0),
        SUBTRACTION(0),
        MULTIPLICATION(1),
        DIVISION(1),
        EXTENSION(2);

        private int priority;

        Operations(int priority){
            this.priority = priority;
        }

        public int getPriority(){
            return priority;
        }

        public double calculation(ArrayList<ExpressionMember> expressions){
            switch (this){
                case ADDITION:{
                    return expressions.get(0).getValue() + expressions.get(1).getValue();
                }
                case SUBTRACTION:{
                    return expressions.get(0).getValue() - expressions.get(1).getValue();
                }
                case MULTIPLICATION:{
                    return expressions.get(0).getValue() * expressions.get(1).getValue();
                }
                case DIVISION:{
                    return expressions.get(0).getValue() / expressions.get(1).getValue();
                }
                case EXTENSION:{
                    return Math.pow(expressions.get(0).getValue(), expressions.get(1).getValue());
                }
                default:{
                    return 0;
                }
            }
        }

        @Override
        public String toString() {
            switch (this){
                case SUBTRACTION:{
                    return "SUBTRACTION";
                }
                case ADDITION:{
                    return "ADDITION";
                }
                case MULTIPLICATION:{
                    return "MULTIPLICATION";
                }
                case DIVISION:{
                    return "DIVISION";
                }
                case EXTENSION:{
                    return "EXTENSION";
                }
            }
            return null;
        }
    }

    static class Engine {
        String[] members = {"\\d+", "\\)", "\\(", "\\-", "\\*", "\\+", "/", "\\^"};
        String template = "";
        String corrector = "";
        Pattern pattern;
        Pattern correctorPattern;
        {
            for (String tmp : members){
                corrector += tmp;
                template += "|" + tmp;
            }
            template = template.substring(1);
            pattern = Pattern.compile(template);
            correctorPattern = Pattern.compile(corrector);
        }
        Matcher matcher;
        ExpressionMember root;
        Stack<String> brackets;

        public Engine(){

        }

        public int read(String expression){
            matcher = pattern.matcher(expression);
            brackets = new Stack<>();
            try {
                root = read();
                if(brackets.empty()) {
                    return (int)root.getValue();
                }else {
                    return 0;
                }
            }catch (NullPointerException e){
                return 0;
            }
        }

        public boolean verify(String expression){
            //matcher = correctorPattern.matcher(expression);
            //System.out.println(correctorPattern.toString());
            //return matcher.find();
            return false;
        }

        private ExpressionMember read() throws NullPointerException{
            ExpressionMember lastMember = null;
            Operations lastOperation = null;
            while(matcher.find()){
                switch (matcher.group()){
                    case "(" : {
                        brackets.push("(");
                        if(lastMember == null){
                            lastMember = read();
                            if(lastMember instanceof  SubExpression) {
                                ((SubExpression) lastMember).setNewLevel();
                            }
                            break;
                        }
                        ArrayList<ExpressionMember> newMain = new ArrayList<>();
                        ExpressionMember tmp = read();
                        if (lastOperation.getPriority() > lastMember.getPriority() ){
                            ((SubExpression)lastMember).rotateExpression(tmp, lastOperation);
                        }else {
                            newMain.add(lastMember);
                            newMain.add(tmp);
                            lastMember = new SubExpression(newMain, lastOperation);
                        }
                        lastOperation = null;
                        break;
                    }
                    case ")" : {
                        if(brackets.empty()){
                            throw new NullPointerException(")");
                        }
                        brackets.pop();
                        return lastMember;
                    }
                    case "+" : {
                        if(lastOperation != null){
                            throw new NullPointerException("+");
                        }
                        lastOperation = Operations.ADDITION;
                        break;
                    }
                    case "-" : {
                        if(lastMember == null){
                            matcher.find();
                            lastMember = new Token(-1 * Double.parseDouble(matcher.group()));
                        }else {
                            if(lastOperation != null){
                                throw new NullPointerException("-");
                            }
                            lastOperation = Operations.SUBTRACTION;
                        }
                        break;
                    }
                    case "/" : {
                        if(lastOperation != null){
                            throw new NullPointerException("/");
                        }
                        lastOperation = Operations.DIVISION;
                        break;
                    }
                    case "*" : {
                        if(lastOperation != null){
                            throw new NullPointerException("*");
                        }
                        lastOperation = Operations.MULTIPLICATION;
                        break;
                    }
                    case "^" : {
                        if(lastOperation != null){
                            throw new NullPointerException("^");
                        }
                        lastOperation = Operations.EXTENSION;
                        break;
                    }
                    default:{
                        if(lastMember != null){
                            if (lastOperation.getPriority() > lastMember.getPriority() ){
                                ((SubExpression)lastMember).rotateExpression(new Token(Double.parseDouble(matcher.group())), lastOperation);
                            }else{
                                ArrayList<ExpressionMember> newTokens = new ArrayList<>();
                                newTokens.add(lastMember);
                                newTokens.add(new Token(Double.parseDouble(matcher.group())));

                                lastMember = new SubExpression(newTokens, lastOperation);
                            }
                        }else{
                            lastMember = new Token(Double.parseDouble(matcher.group()));
                        }
                        lastOperation = null;
                    }
                }
            }
            return lastMember;
        }
    }

    public static void main(String argc[]) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String s;
        s=r.readLine(); //получаем строку
        r.close();
        Engine engine = new Engine();
        System.out.println(engine.read(s));
    }
}
