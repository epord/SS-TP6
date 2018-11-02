package Helpers;

import javafx.util.Pair;

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


    public static StringBuilder plot(List<List<Double>> lists, String xLabel, String yLabel){
        StringBuilder sb = new StringBuilder();

        defineLists(lists, "a", sb);

        sb.append("plot(");
        for (int i = 0; i < lists.size(); i++) {
            sb.append("[1:").append(lists.get(i).size())
                    .append("],a").append(i).append(",");
        }
        sb.setLength(sb.length()-1);
        sb.append(")\n");

        sb.append("xlabel (\"" + xLabel + "\");\n");
        sb.append("ylabel (\"" + yLabel + "\");\n");
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

    public static StringBuilder plotMean(List<List<Double>> lists, String xLabel, String yLabel){
        StringBuilder sb = new StringBuilder();

        defineLists(lists, "a", sb);
        addOperator("_mean","mean", "a", lists,sb);
        addOperator("_std","std", "a", lists,sb);

        sb.append("errorbar([1:size(_mean)(2)], _mean, _std)\n");

        sb.append("xlabel (\"" + xLabel + "\");\n");
        sb.append("ylabel (\"" + yLabel + "\");\n");
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

        sb.append("stds=[");
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

    public static StringBuilder plotFlow(List<List<Pair<Double, Double>>> flows, String xLabel, String yLabel){
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < flows.size(); i++) {
            sb.append("t"+i).append("=[");
            for (int j = 0; j < flows.get(i).size(); j++) {
                sb.append(flows.get(i).get(j).getKey()).append(",");
            }
            sb.setLength(sb.length()-1);
            sb.append("];\n");

            sb.append("f"+i).append("=[");
            for (int j = 0; j < flows.get(i).size(); j++) {
                sb.append(flows.get(i).get(j).getValue()).append(",");
            }
            sb.setLength(sb.length()-1);
            sb.append("];\n");
        }

        sb.append("plot(");
        for (int i = 0; i < flows.size(); i++) {
            sb.append("t"+i).append(",f").append(i).append(",");
        }
        sb.setLength(sb.length()-1);
        sb.append(")\n");

        sb.append("xlabel (\"" + xLabel + "\");\n");
        sb.append("ylabel (\"" + yLabel + "\");\n");
        return sb;
    }

    public static StringBuilder plotFlowMean(List<List<Pair<Double, Double>>> flows, String xLabel, String yLabel){
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < flows.size(); i++) {
            sb.append("t"+i).append("=[");
            for (int j = 0; j < flows.get(i).size(); j++) {
                sb.append(flows.get(i).get(j).getKey()).append(",");
            }
            sb.setLength(sb.length()-1);
            sb.append("];\n");

            sb.append("f"+i).append("=[");
            for (int j = 0; j < flows.get(i).size(); j++) {
                sb.append(flows.get(i).get(j).getValue()).append(",");
            }
            sb.setLength(sb.length()-1);
            sb.append("];\n");
        }

        sb.append("_mean=mean([");
        for (int i = 0; i < flows.size(); i++) {
            sb.append("f").append(i).append(";");
        }
        sb.setLength(sb.length()-1);
        sb.append("]);\n");

        sb.append("_std=std([");
        for (int i = 0; i < flows.size(); i++) {
            sb.append("f").append(i).append(";");
        }
        sb.setLength(sb.length()-1);
        sb.append("]);\n");

        sb.append("errorbar(t0, _mean, _std)\n");

        sb.append("xlabel (\"" + xLabel + "\");\n");
        sb.append("ylabel (\"" + yLabel + "\");\n");
        return sb;
    }
}
