package ku.cs.duckdealer.services;

import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.transform.Scale;

public class PrintService {

    public void print(Node node, double width, double height) {
        Printer printer = Printer.getDefaultPrinter();
        PrinterJob job = PrinterJob.createPrinterJob();
        job.setPrinter(printer);
        double scaleX = width / node.getBoundsInParent().getWidth();
        double scaleY = height / node.getBoundsInParent().getHeight();
        Scale scale = new Scale(scaleX, scaleY);
        node.getTransforms().add(scale);
        if (job != null) {
            boolean success = job.printPage(node);
            if (success) {
                job.endJob();
            }
        } else {
            System.out.println("Can't find printer.");
        }
        node.getTransforms().remove(scale);
    }

}
