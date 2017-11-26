package ku.cs.duckdealer.services;

import com.sun.javafx.print.PrintHelper;
import com.sun.javafx.print.Units;
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
