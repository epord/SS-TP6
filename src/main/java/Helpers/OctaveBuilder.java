package Helpers;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class OctaveBuilder {

    public OctaveBuilder() {
    }

    public static StringBuilder addList(String name, List<Double> list, StringBuilder sb) {
        // Octave output
        sb.append(name).append("=[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(",");
        }
        sb.setLength(sb.length()-1);
        sb.append("];\n");
        return sb;
    }



    public static StringBuilder plot(List<List<Double>> lists){
        StringBuilder sb = new StringBuilder();

        defineLists(lists,sb);

        sb.append("plot(");
        for (int i = 0; i < lists.size(); i++) {
            sb.append("[1:").append(lists.get(i).size())
                    .append("],a").append(i).append(",");
        }
        sb.setLength(sb.length()-1);
        sb.append(")\n");
        return sb;
    }

    public static StringBuilder addOperator(String name, String operator, List<?> list, StringBuilder sb){
        sb.append(name).append("=").append(operator).append("([");
        for (int i = 0; i < list.size(); i++) {
            sb.append("a"+i).append(";");
        }
        sb.setLength(sb.length()-1);
        sb.append("]);\n");
        return sb;
    }

    public static StringBuilder plotMean(List<List<Double>> lists){
        StringBuilder sb = new StringBuilder();

        defineLists(lists,sb);
        addOperator("mean","mean",lists,sb);
        addOperator("std","std",lists,sb);

        sb.append("errorbar([1:size(mean)(2)], mean, std)\n");
        return sb;
    }
    
    public static StringBuilder plotEscape(List<List<List<Double>>> lists, List<Double> velocities){
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < lists.size(); i++) {
            addOperator("std"+i,"std",lists.get(i),sb);
            addOperator("mean"+i,"mean",lists.get(i),sb);
        }

        sb.append("means=[");
        for (int i = 0; i < lists.size(); i++) {
            sb.append("mean"+i).append("(end),");
        }
        removeLast(sb);
        sb.append("]");

        sb.append("stds=[");
        for (int i = 0; i < lists.size(); i++) {
            sb.append("std"+i).append("(end),");
        }
        removeLast(sb);
        sb.append("]");

        defineLists(Arrays.asList(velocities),sb);
        sb.append("errorbar(a0,means,stds)");
        return sb;
    }

    public static StringBuilder removeLast(StringBuilder sb){
        sb.setLength(sb.length());
        return sb;
    }

    public static StringBuilder defineLists(List<List<Double>> lists, StringBuilder sb){
        for (int i = 0; i < lists.size(); i++) {
            addList("a"+i,lists.get(i),sb);
        }
        return sb;
    }
}
