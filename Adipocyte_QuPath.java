import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;



public class Adipocyte_QuPath implements PlugIn {

    public void run(String arg) {
//Create dialog box to explain plugin and ask for user input.
        GenericDialog dialog = new GenericDialog("Adipocyte Counter User Input");
            dialog.addMessage("Welcome to the Adipocyte QuPath Plugin! Please note: "
              + "\nThis program assumes you have used the ImageJ extension "
              + "from QuPath, with a downsample factor of 4.");
            dialog.addMessage("Please input the minimum and maximum area "
              + "of the adipocytes you wish to have measured in square \u00B5m.");
            dialog.addNumericField("Area min (sq \u00B5m):", 500, 0);
            dialog.addNumericField("Area max (sq \u00B5m):", 20000, 0);
            dialog.addMessage("Please input the minimum and maximum circularity"
              + "of the adipocytes you wish to have measured. "
              + "\nThis is on a scale of 0-1, with 0 being a straight line "
              + "and 1 being a perfect circle.");
            dialog.addNumericField("Circularity min:", 0.30, 2);
            dialog.addNumericField("Circularity max:", 1.0, 2);
	          dialog.addCheckbox("Check if inversion is needed", false);
            dialog.addMessage("After the analysis has run, "
              + "the cells counted will be highlighted in blue. "
              + "\nIf the blue outlines are NOT around adipocytes, "
              + "please close the current image, \nre-open the original image, "
              + "and re-run the analysis with the Inversion Box checked above. "
              + "\nYou can follow the same procedure if you would like to adjust"
              + "any other parameters.");
            dialog.showDialog();

            if (dialog.wasCanceled()) {
                IJ.error("PlugIn cancelled!");
                return;
            }

//Retrieve variables that were user-defined from dialog box
            double rangeMin = dialog.getNextNumber();
            double rangeMax = dialog.getNextNumber();
            double shapeMin = dialog.getNextNumber();
            double shapeMax = dialog.getNextNumber();
	          boolean inversion = dialog.getNextBoolean();

//Run plugin within the parameters defined by user
        ImagePlus imp = IJ.getImage();
            IJ.run(imp, "Find Edges", ""); //Preprocessing: Find Edges 3x
            IJ.run(imp, "Find Edges", "");
            IJ.run(imp, "Find Edges", "");
            Prefs.blackBackground = false;
            IJ.run(imp, "Make Binary", ""); //Make binary using auto-threshold
            /*Sometimes, the binary is inverted. The user can re-run the PlugIn
            with the invert box checked to activate the following:*/
	          if (inversion) {
		            IJ.run(imp, "Invert", "");
	             }
            /*Analysis of each cell follows, using size and circularity parameters
            inputed by the user. The output is the original image with blue
            outlines to verify that adipocytes were segmented correctly, and
            results and summary windows*/
            IJ.run(imp, "Analyze Particles...", "size=" + rangeMin + "-"
            + rangeMax + " circularity=" + shapeMin + "-" + shapeMax
            + " show=Overlay display summarize in_situ");
    }
}
