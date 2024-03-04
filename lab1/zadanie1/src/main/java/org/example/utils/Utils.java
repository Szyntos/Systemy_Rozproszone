package org.example.utils;

public class Utils {
    public static String extractWithoutPrefix(String prefix, String line){
        String extracted = line.substring(line.indexOf(prefix) + prefix.length());
        if (line.contains(prefix)) {
            return extracted;
        } else {
            System.out.println("Failed Extraction");
        }
        return "Error";

    }
    public static String asciiArtPC = """
                                     ______                    \s
             _________        .---""\"      ""\"---.             \s
            :______.-':      :  .--------------.  :            \s
            | ______  |      | :                : |            \s
            |:______B:|      | |  Little Error: | |            \s
            |:______B:|      | |                | |            \s
            |:______B:|      | |  Power not     | |            \s
            |         |      | |  found.        | |            \s
            |:_____:  |      | |                | |            \s
            |    ==   |      | :                : |            \s
            |       O |      :  '--------------'  :            \s
            |       o |      :'---...______...---'             \s
            |       o |-._.-i___/'             \\._             \s
            |'-.____o_|   '-.   '-...______...-'  `-._         \s
            :_________:      `.____________________   `-.___.-.\s
                             .'.eeeeeeeeeeeeeeeeee.'.      :___:
                fsc        .'.eeeeeeeeeeeeeeeeeeeeee.'.        \s
                          :____________________________:
            
            """;
    public static String asciiArtCat = """
                   ,
                   \\`-._           __
                    \\\\  `-..____,.'  `.
                     :`.         /    \\`.
                     :  )       :      : \\
                      ;'        '   ;  |  :
                      )..      .. .:.`.;  :
                     /::...  .:::...   ` ;
                     ; _ '    __        /:\\
                     `:o>   /\\o_>      ;:. `.
                    `-`.__ ;   __..--- /:.   \\
                    === \\_/   ;=====_.':.     ;
                     ,/'`--'...`--....        ;
                          ;                    ;
                        .'                      ;
                      .'                        ;
                    .'     ..     ,      .       ;
                   :       ::..  /      ;::.     |
                  /      `.;::.  |       ;:..    ;
                 :         |:.   :       ;:.    ;
                 :         ::     ;:..   |.    ;
                  :       :;      :::....|     |
                  /\\     ,/ \\      ;:::::;     ;
                .:. \\:..|    :     ; '.--|     ;
               ::.  :''  `-.,,;     ;'   ;     ;
            .-'. _.'\\      / `;      \\,__:      \\
            `---'    `----'   ;      /    \\,.,,,/
                               `----`              fsc
            
            """;

    public String getAsciiArt(int type){
        if (type == 0){
            return asciiArtCat;
        }
        return asciiArtPC;
    }
}
