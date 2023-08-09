import dto.MeasurementsDTO;
import dto.MeasurementsResponse;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class chartDraw {
    public static void main(String[] args) {
        List<Double> temperatures = getMeasurementValue();
        drawChart(temperatures);
    }

    private static List<Double> getMeasurementValue() {
        RestTemplate restTemplate = new RestTemplate();
        final String url = "http://localhost:8085/measurements";

        MeasurementsResponse measurementsResponse = restTemplate.getForObject(url, MeasurementsResponse.class);
        if (measurementsResponse == null || measurementsResponse.getMeasurements() == null) {
            return Collections.emptyList();
        } else {
            return measurementsResponse.getMeasurements().stream().map(MeasurementsDTO::getValue)
                    .collect(Collectors.toList());
        }
    }

    private static void drawChart(List<Double> temps) {
        double[] xData = IntStream.range(0, temps.size()).asDoubleStream().toArray();
        double[] yData = temps.stream().mapToDouble(y -> y).toArray();

        XYChart chart = QuickChart
                .getChart("Temperatures", "X", "Y", "temperature", xData, yData);

        new SwingWrapper(chart).displayChart();
    }
}
