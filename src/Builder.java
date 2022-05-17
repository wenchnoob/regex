import java.util.*;

public class Builder {

    private String src;
    private List<Token> tokenList;
    private int pos = 0;

    public Builder(String src) {
        this.src = src;
    }


    public State build() {
        tokenList();
        State s = new State(false,new Hashtable<Character, List<State>>(){{
            put('\0', Arrays.asList(new State(false, regexp())));
        }});
        s.appendEdge('\0', new State(true));
        return s;
    }

    private Hashtable<Character, List<State>> regexp() {
        State lhs = quantifier();
        if (true) return new Hashtable<Character, List<State>>() {{ put('\0', Arrays.asList(lhs)); }};;
        if (pos >= tokenList.size() - 1) return new Hashtable<Character, List<State>>() {{ put(tokenList.get(pos).val, Arrays.asList(lhs)); }};
        else return new Hashtable<Character, List<State>>() {{ put(tokenList.get(pos++).val, Arrays.asList(lhs, new State(false, regexp()))); }};
    }

    private State quantifier() {
        return character();
    }

    private  State character() {
        Token cur = tokenList.get(pos);
        if (cur.type == Token.Type.LITERAL) {
            pos++;
            return new State(false, cur.val);
        } else if (cur.type == Token.Type.LPAREN) {
            pos++;
            Hashtable<Character, List<State>> states = regexp();
            if (tokenList.get(pos).type != Token.Type.RPAREN) fail();
            pos++;
            return new State(false, states);
        } else {
            fail();
        }
        return null;
    }


    private void fail() {
        throw new IllegalStateException("illegal context " + ((pos - 1 >= 0) ? tokenList.get(pos-1) : "") + tokenList.get(pos) + ((pos + 1 < tokenList.size()) ? tokenList.get(pos+1) : "") );
    }


    public List<Token> tokenList() {
        if (Objects.nonNull(tokenList)) return tokenList;

        List<Token> tokenList = new LinkedList<>();
        for (int i = 0; i < src.length(); i++) {
            char cur = src.charAt(i);
            switch (cur) {
                case '*':
                    tokenList.add(new Token(Token.Type.STAR));
                    break;
                case '+':
                    tokenList.add(new Token(Token.Type.PLUS));
                    break;
                case '.':
                    tokenList.add(new Token(Token.Type.DOT));
                    break;
                case '(':
                    tokenList.add(new Token(Token.Type.LPAREN));
                    break;
                case ')':
                    tokenList.add(new Token(Token.Type.RPAREN));
                    break;
                case '|':
                    tokenList.add(new Token(Token.Type.BAR));
                    break;
                case '\\':
                    cur = src.charAt(++i);
                default:
                    tokenList.add(new Token(cur));
            }
        }

        this.tokenList = tokenList;
        return tokenList;
    }

    public static class Node {

        private char val;
        private Type type;

        public Node(char val, Type type) {
            this.val = val;
            this.type = type;
        }

        public Node(char val) {
            this(val, Type.LITERAL);
        }

        public Node(Type type) {
            this('\0', type);
        }

        enum Type {
            LITERAL, OR, ANY, ZERO_MORE, ONE_PLUS;
        }
    }

    public static class Token {
        private char val;
        private Type type;

        public Token(char val, Type type) {
            this.val = val;
            this.type = type;
        }

        public Token(char val) {
            this(val, Type.LITERAL);
        }

        public Token(Type type) {
            this('\0', type);
        }

        public String toString() {
            return String.format("Token(val = %s, type = %s)", val, type);
        }

        enum Type {
            LITERAL, STAR, DOT, PLUS, BAR, LPAREN, RPAREN
        };
    }

}
