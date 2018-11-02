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

        defineLists(lists, "a", sb);

        sb.append("plot(");
        for (int i = 0; i < lists.size(); i++) {
            sb.append("[1:").append(lists.get(i).size())
                    .append("],a").append(i).append(",");
        }
        sb.setLength(sb.length()-1);
        sb.append(")\n");

        sb.append("xlabel (\"Cantidad de particulas evacuadas\");\n");
        sb.append("ylabel (\"Tiempo [s]\");\n");
        return sb;
    }

    public static StringBuilder addOperator(String name, String operator, String variableName, List<?> list, StringBuilder sb){
        sb.append(name).append("=").append(operator).append("([");
        for (int i = 0; i < list.size(); i++) {
            sb.append(variableName).append(i).append(";");
        }
        sb.setLength(sb.length()-1);
        sb.append("]);\n");
        return sb;
    }

    public static StringBuilder plotMean(List<List<Double>> lists){
        StringBuilder sb = new StringBuilder();

        defineLists(lists, "a", sb);
        addOperator("_mean","mean", "a", lists,sb);
        addOperator("_std","std", "a", lists,sb);

        sb.append("errorbar([1:size(_mean)(2)], _mean, _std)\n");

        sb.append("xlabel (\"Cantidad de particulas evacuadas\");\n");
        sb.append("ylabel (\"Tiempo [s]\");\n");
        return sb;
    }
    
    public static StringBuilder plotEscape(List<List<List<Double>>> lists, List<Double> velocities){
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < lists.size(); i++) {
            defineLists(lists.get(i), "a"+i, sb);
            addOperator("_std"+i,"std", "a"+i, lists.get(i),sb);
            addOperator("_mean"+i,"mean", "a"+i, lists.get(i),sb);
        }

        sb.append("means=[");
        for (int i = 0; i < lists.size(); i++) {
            sb.append("_mean"+i).append("(end),");
        }
        removeLast(sb);
        sb.append("];\n");

        sb.append("_stds=[");
        for (int i = 0; i < lists.size(); i++) {
            sb.append("_std"+i).append("(end),");
        }
        removeLast(sb);
        sb.append("];\n");

        defineLists(Arrays.asList(velocities), "vel", sb);

        sb.append("errorbar(vel0,means,stds)\n");
        sb.append("xlabel (\"Velocidad maxima [m/s]\");\n");
        sb.append("ylabel (\"Tiempo total de evacuacion [s]\");\n");
        return sb;
    }

    public static StringBuilder removeLast(StringBuilder sb){
        sb.setLength(sb.length()-1);
        return sb;
    }

    public static StringBuilder defineLists(List<List<Double>> lists, String variableName, StringBuilder sb){
        for (int i = 0; i < lists.size(); i++) {
            addList(variableName+i,lists.get(i),sb);
        }
        return sb;
    }
}
