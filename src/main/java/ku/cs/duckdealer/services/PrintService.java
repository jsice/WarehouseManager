package ku.cs.duckdealer.services;

import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.transform.Scale;

public class PrintService {

    public void print(Node node, double width, double height) {
        PrinterJob job = PrinterJob.createPrinterJob();
//        job.setPrinter(printer);
        double scaleX = width / node.getBoundsInParent().getWidth();
        double scaleY = height / node.getBoundsInParent().getHeight();
        Scale scale = new Scale(scaleX, scaleY);
        node.getTransforms().add(scale);
        if (job != null) {
            boolean success = job.printPage(node);
            System.out.println("printing...");
            if (success) {
                job.endJob();
                System.out.println("printing success.");
            }
        } else {
            System.out.println("Can't find printer.");
        }
        node.getTransforms().remove(scale);
    }

}
