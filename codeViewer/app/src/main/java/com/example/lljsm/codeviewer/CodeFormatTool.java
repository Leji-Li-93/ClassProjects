package com.example.lljsm.codeviewer;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class CodeFormatTool {

    public final static String KEY_COMMENTS = "comments";
    public final static String KEY_KEYWORDS = "keywords";
    public final static String KEY_STRINGS = "strings";
    public final static String KEY_CHARS = "chars";
    public final static String KEY_NUMBERS = "numbers";
    public final static String KEY_REGULARWORDS = "words";
    public final static String KEY_OPERATORS = "operators";
    public final static String KEY_BACKGROUND_ODD = "odd";
    public final static String KEY_BACKGROUND_EVEN = "even";

    private static String[] key_words_java = {
            "abstract",
            "continue",
            "for",
            "new",
            "switch",
            "assert",
            "default",
            "goto",
            "package",
            "synchronized",
            "boolean",
            "do",
            "if",
            "private",
            "this",
            "break",
            "double",
            "implements",
            "protected",
            "throw",
            "byte",
            "else",
            "import",
            "public",
            "throws",
            "case",
            "enum",
            "instanceof",
            "return",
            "transient",
            "catch",
            "extends",
            "int",
            "short",
            "try",
            "char",
            "final",
            "interface",
            "static",
            "void",
            "class",
            "finally",
            "long",
            "strictfp",
            "volatile",
            "const",
            "float",
            "native",
            "super",
            "while"
    };
    private static String[] key_words_cpp = {
            "Asm",
            "auto",
            "bool",
            "break",
            "case",
            "catch",
            "char",
            "class",
            "const_cast",
            "continue",
            "default",
            "delete",
            "do",
            "double",
            "else",
            "enum",
            "dynamic_cast",
            "extern",
            "false",
            "float",
            "for",
            "union",
            "unsigned",
            "using",
            "friend",
            "goto",
            "if",
            "inline",
            "int",
            "long",
            "mutable",
            "virtual",
            "namespace",
            "new",
            "operator",
            "private",
            "protected",
            "public",
            "register",
            "void",
            "reinterpret_cast",
            "return",
            "short",
            "signed",
            "sizeof",
            "static",
            "static_cast",
            "volatile",
            "struct",
            "switch",
            "template",
            "this",
            "throw",
            "true",
            "try",
            "typedef",
            "typeid",
            "unsigned",
            "wchar_t",
            "while"
    };

    public static char[] operators = {
            33,
            34,
            35,
            37,
            38,
            39,
            40,
            41,
            42,
            43,
            44,
            45,
            46,
            47,
            58,
            59,
            60,
            61,
            62,
            63,
            64,
            91,
            92,
            93,
            94,
            96,
            123,
            124,
            125
    };

    public static String color_file_name = "CodeViewerColor.txt";
    public static String[] default_color = {
            "#003b9b", // for keywords
            "#adadad", // for comments
            "#00720b", // for strings
            "#303030", // for regular words
            "#0036ff", // for numbers
            "#ffae00", // for chars
            "#ff0094", // for operators
            "#f1f1f1", // for odd background
            "#ffffff"  // for even background
    };
    public static String[] default_color_key_name = {
            KEY_KEYWORDS,
            KEY_COMMENTS,
            KEY_STRINGS,
            KEY_REGULARWORDS,
            KEY_NUMBERS,
            KEY_CHARS,
            KEY_OPERATORS,
            KEY_BACKGROUND_ODD,
            KEY_BACKGROUND_EVEN
    };

    public static String format_color_string(String color){
        if(color.length() > 7){
            color = color.substring(3, color.length());
            color = '#' + color;
        }
        return color;
    }

    // Converse color hex string to rgb values
    public static int[] hex2RGB(String color){
        Log.i("222", color);
        int[] RGB = new int[3];
        int tmp;
        for (int i = 0; i < 3; i++) {
            tmp = hex2int(color.charAt(i * 2 + 1 ));
            tmp = tmp * 16 + hex2int(color.charAt((i+1) * 2));
            RGB[i] = tmp;
        }
        return RGB;
    }

    // Converse hex code to integer
    public static int hex2int(char c){
        if(c >= '0' && c <= '9'){
            return (c - '0');
        }else if(c >= 'A' && c <= 'F'){
            return (c - 'A' + 10);
        } else if(c >= 'a' && c <= 'f'){
            return (c - 'a' + 10);
        }
        return 0;
    }

    public static String RGB2hex(int[] rgb){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('#');
        int length = rgb.length;
        for (int i = 0; i < length; i++) {
            stringBuilder.append(int2hex(rgb[i]));
        }
        return stringBuilder.toString();
    }

    public static String int2hex(int n){
        int lower = n % 16;
        int higher = n / 16;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(int2hex_char(higher));
        stringBuilder.append(int2hex_char(lower));
        return stringBuilder.toString();
    }

    // get single char
    private static char int2hex_char(int n){
        if (n >= 10){
            int tmp = n % 10;
            return (char)('A' + tmp);
        } else {
            return (char)('0' + n);
        }
    }

    // save or update color file
    public static void save_to_color_file(Context context, HashMap<String, String> colors) throws IOException {
        File file = new File(context.getFilesDir(), color_file_name);
        file.delete();
        file.createNewFile();
        FileOutputStream fileOutputStream = context.openFileOutput(color_file_name, Context.MODE_PRIVATE);
        int length = default_color_key_name.length;
        for (int i = 0; i < length; i++) {
            Log.i("222", "save to file -> " + colors.get(default_color_key_name[i]));
            fileOutputStream.write(colors.get(default_color_key_name[i]).getBytes());
            fileOutputStream.write("\n".getBytes());
        }
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public static HashMap<String, String> read_from_color_file(Context context) throws IOException {
        HashMap<String, String> color_map = new HashMap<String, String>();
        File file = new File(context.getFilesDir(), color_file_name);
        /*if (file.exists()){
            file.delete();
        }*/
        if(!file.exists()){
            Log.i("222", "Color file not exit");
            int length = default_color.length;
            for (int i = 0; i < length; i++) {
                color_map.put(default_color_key_name[i], default_color[i]);
            }
            try{
                save_to_color_file(context, color_map);
            } catch (IOException e){
                Toast.makeText(context, "Cannot save file", Toast.LENGTH_LONG).show();
                return color_map;
            }
        } else {
            FileInputStream fileInputStream = context.openFileInput(color_file_name);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = null;
            int length = default_color.length;
            for (int i = 0; i < length; i++) {
                line = reader.readLine();
                color_map.put(default_color_key_name[i], line);
            }
            reader.close();
            inputStreamReader.close();
            fileInputStream.close();
        }
        return color_map;
    }

    public static ArrayList<String> attach_color_to_code(ArrayList<String> codes, HashMap<String, String> colors){
        boolean isComment = false;
        ArrayList<String> colored_codes = new ArrayList<String>(codes.size());

        int length = codes.size();
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            // once a line was loaded, stringBuilder should be reset
            stringBuilder.delete(0, stringBuilder.length());
            // load a new line
            line = codes.get(i);
            int n = line.length();
            for (int j = 0; j < n; j++) {
                // multi line comment
                if(isComment && ((j+1) < length)){
                    stringBuilder.append(CodeFormatTool.html_font_head(colors.get(KEY_COMMENTS)));
                    while ((j + 1) < n){
                        stringBuilder.append(line.charAt(j));
                        // the end of multi line comment
                        if(line.charAt(j) == '*' && line.charAt(j + 1) == '/'){
                            j++;
                            stringBuilder.append(line.charAt(j));
                            isComment = false;
                            break;
                        }
                        j++; // move to next char
                    }
                    stringBuilder.append(html_font_tail());
                    //j--;
                }
                // the head of multi line comment
                else if((j + 1) < n && line.charAt(j) == '/' && line.charAt(j + 1) == '*'){
                    stringBuilder.append(html_font_head(colors.get(KEY_COMMENTS)));
                    stringBuilder.append(line.charAt(j));
                    stringBuilder.append(line.charAt(j + 1));
                    isComment = true;
                    j += 2; // because we append 2 char, the index should be moved to 2 position forward
                    while ((j + 1) < n){
                        stringBuilder.append(line.charAt(j));
                        // the end of the multi line comment
                        if(line.charAt(j) == '*' && line.charAt(j + 1) == '/'){
                            stringBuilder.append(line.charAt(j + 1));
                            j++;
                            isComment = false;
                            break;
                        }
                        j++; // move to next char
                    }
                    stringBuilder.append(html_font_tail());
                    //j--;
                }
                // single line comment
                else if((j + 1) < n && line.charAt(j) == '/' && line.charAt(j + 1) == '/'){
                    stringBuilder.append(html_font_head(colors.get(KEY_COMMENTS)));
                    // cut the rest code
                    stringBuilder.append(line.substring(line.indexOf("//")));
                    stringBuilder.append(html_font_tail());
                    break; // read the next line
                }
                // string
                else if (line.charAt(j) == '\"'){
                    stringBuilder.append(html_font_head(colors.get(KEY_STRINGS)));
                    stringBuilder.append(line.charAt(j));
                    j++;
                    while(j < n){
                        stringBuilder.append(line.charAt(j));
                        // the end
                        if(line.charAt(j) == '\"' && line.charAt(j - 1) != '\\'){
                            break;
                        }
                        j++;
                    }
                    stringBuilder.append(html_font_tail());
                    //j--;
                }
                // char
                else if (line.charAt(j) == '\''){
                    stringBuilder.append(html_font_head(colors.get(KEY_CHARS)));
                    stringBuilder.append(line.charAt(j));
                    j++;
                    while(j < n){
                        stringBuilder.append(line.charAt(j));
                        // the end
                        if(line.charAt(j) == '\'' && line.charAt(j - 1) != '\\'){
                            break;
                        }
                        j++;
                    }
                    stringBuilder.append(html_font_tail());
                    //j--;
                }
                // keywords
                else if(isLetter(line.charAt(j))){
                    StringBuilder wordBuilder = new StringBuilder();
                    while(j < n && (isLetter(line.charAt(j)) || isNum(line.charAt(j)))){
                        wordBuilder.append(line.charAt(j));
                        j++;
                    }
                    // is key word
                    if (isKeyworld(wordBuilder.toString())){
                        stringBuilder.append(html_font_head(colors.get(KEY_KEYWORDS)));
                    } else { // not a keyword
                        stringBuilder.append(html_font_head(colors.get(KEY_REGULARWORDS)));
                    }
                    stringBuilder.append(wordBuilder.toString());
                    stringBuilder.append(html_font_tail());
                    j--;
                }
                // number
                else if(isNum(line.charAt(j))){
                    // the letter is not going after a letter
                    if((j == 0 || !isLetter(line.charAt(j - 1)))){
                        stringBuilder.append(html_font_head(colors.get(KEY_NUMBERS)));
                        while(j < n && (isNum(line.charAt(j)) || line.charAt(j) == '.' || isLetter(line.charAt(j)))){
                            stringBuilder.append(line.charAt(j));
                            j++;
                        }
                        stringBuilder.append(html_font_tail());
                        j--;
                    } else {
                        stringBuilder.append(html_font_head(colors.get(KEY_REGULARWORDS)));
                        stringBuilder.append(line.charAt(j));
                        stringBuilder.append(html_font_tail());
                    }
                }
                // operator
                else if(isOperators(line.charAt(j))){
                    stringBuilder.append(html_font_head(colors.get(KEY_OPERATORS)));
                    stringBuilder.append(char_to_html(line.charAt(j)));
                    stringBuilder.append(html_font_tail());
                }
                // space
                else if(isSpace(line.charAt(j))){
                    stringBuilder.append(html_font_head(colors.get(KEY_REGULARWORDS)));
                    while(j < n && isSpace(line.charAt(j))){
                        stringBuilder.append(char_to_html(line.charAt(j)));
                        j++;
                    }
                    j--;
                    stringBuilder.append(html_font_tail());
                }
                //else
                else {
                    stringBuilder.append(html_font_head(colors.get(KEY_REGULARWORDS)));
                    stringBuilder.append(char_to_html(line.charAt(j)));
                    stringBuilder.append(html_font_tail());
                }
            }
            colored_codes.add(stringBuilder.toString());
        }
        for (int i = 0; i < colored_codes.size(); i++) {
            Log.i("333", colored_codes.get(i));
        }
        return colored_codes;
    }

    public static boolean isLetter(char c){
        if (c >= 'A' && c <= 'Z')
            return true;
        else if( c >= 'a' && c <= 'z')
            return true;
        else
            return false;
    }

    public static boolean isNum(char c){
        if (c >= '0' && c <= '9')
            return true;
        else
            return false;
    }

    public static boolean isKeyworld(String s){
        s = s.trim();
        int length = key_words_java.length;
        for (int i = 0; i < length; i++) {
            if (s.equals(key_words_java[i]))
                return true;
        }
        length =  key_words_cpp.length;
        for (int i = 0; i < length; i++) {
            if (s.equals(key_words_cpp[i]))
                return true;
        }
        return false;
    }

    public static boolean isOperators(char c){
        int length = operators.length;
        for (int i = 0; i < length; i++) {
            if(c == operators[i])
                return true;
        }

        return false;
    }

    public static boolean isSpace(char c){
        if (c == 32)
            return true;
        return false;
    }

    public static String html_font_head(String color){
        return ("<font color = \"" + color + "\">");
    }

    public static String html_font_tail(){
        return "</font>" ;
    }

    // convert char to html symbol
    public static String char_to_html(char ch) {
        if (ch == '&') {
            return "&amp;";
        } else if (ch == ' ') { // double the space in order to have a better layout
            return "&nbsp;&nbsp;";
        } else if (ch == '<') {
            return "&lt;";
        } else if (ch == '>') {
            return "&gt;";
        } else if (ch == 9){
            // tab, 1 tab = 4 spaces, just like space, double it to have a better layout
            return "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
        }else {
            return "" + ch;
        }
    }
}
