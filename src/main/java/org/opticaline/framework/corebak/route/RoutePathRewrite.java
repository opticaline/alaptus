package org.opticaline.framework.corebak.route;

import org.opticaline.framework.corebak.exception.IllegalPathException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nathan on 14-9-5.
 */
public class RoutePathRewrite {
    private static final String PREFIX_LEFT = "<";
    private static final String PREFIX_RIGHT = ">";

    private static final Map<String, String> types = new HashMap<>();

    static {
        types.put("int", "\\d");
        types.put("string", ".");
    }

    public static String rewrite(String path) throws IllegalPathException {
        try {
            if (path.indexOf(PREFIX_LEFT) != -1 && path.indexOf(PREFIX_RIGHT) != -1) {
                String temp[] = path.split(PREFIX_LEFT, 2);
                String header = temp[0];
                temp = temp[1].split(PREFIX_RIGHT, 2);
                return new StringBuffer(header).append(format(temp[0])).append(rewrite(temp[1])).toString();
            } else {
                return path;
            }
        } catch (Exception e) {
            throw new IllegalPathException("Error expression: [" + path + "].");
        }
    }

    private static String format(String pre) {
        String[] temp = pre.split("\\s*,\\s*");
        if (temp.length > 1) {
            for (int i = 1; i < temp.length; i++) {
                System.err.println(temp[0]);
            }
        }
        temp = temp[0].split(":");
        String name, length = "{start,end}", type = temp[0];
        if (temp[1].indexOf("[") != -1) {
            temp = temp[1].split("(\\[|\\])");
            name = temp[0];
            temp = temp[1].split("\\-");
            length = length.replaceAll("start", temp[0]).replaceAll("end", temp.length > 1 ? temp[1] : "");
        } else {
            name = temp[1];
            length = "*";
        }
        return "(?<{name}>{type}{length})".replaceAll("\\{name\\}", name)
                .replaceAll("\\{type\\}", types.get(type))
                .replaceAll("\\{length\\}", length);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(RoutePathRewrite.rewrite("Test<int:testint[0-2]>Url"));
        System.out.println(RoutePathRewrite.rewrite("Test<int:testint, length=[0-2]><string:teststring>"));
        System.out.println(RoutePathRewrite.rewrite("<int:testint, length=[0-2]>Url"));
    }
}
