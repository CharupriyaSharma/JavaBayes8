package scriptbayes;

/**
 * JavaBayes.java
 *
 * @author Fabio G. Cozman Copyright 1996 - 1999, Fabio G. Cozman, Carnergie
 * Mellon University, Universidade de Sao Paulo fgcozman@usp.br,
 * http://www.cs.cmu.edu/~fgcozman/home.html
 *
 * The JavaBayes distribution is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation (either version 2 of the License or, at your
 * option, any later version), provided that this notice and the name of the
 * author appear in all copies. Upon request to the author, some of the packages
 * in the JavaBayes distribution can be licensed under the GNU Lesser General
 * Public License as published by the Free Software Foundation (either version 2
 * of the License, or (at your option) any later version). If you're using the
 * software, please notify fgcozman@usp.br so that you can receive updates and
 * patches. JavaBayes is distributed "as is", in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU
 * General Public License along with the JavaBayes distribution. If not, write
 * to the Free Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139,
 * USA.
 */
import JavaBayesInterface.*;
import InferenceGraphs.*;
import InterchangeFormat.IFException;

import java.awt.*;
import java.applet.Applet;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;

public class JavaBayes extends Applet {
    // Graphical elements of JavaBayes

    EditorFrame editorFrame;
    JavaBayesConsoleFrame consoleFrame;

    // Applet flags
    public boolean is_applet = false;

    /**
     * Main method for JavaBayes.
     */
    public static void main(String argv[]) throws IOException, IFException {
        InferenceGraph ig = new InferenceGraph(argv[0]);
        InferenceGraph ig1 = new InferenceGraph(argv[1]);
        // InferenceGraph ig2 = new InferenceGraph(argv[2]);

//        ig.print_bayes_net(System.out);
//        ig.setObservations("dysp", "no");
//        ig.setObservations("xray", "yes");
//        ig.setObservations("either", "no");
//        ig.setObservations("smoke", "yes");
//        System.out.println(ig.get_nodes());
//        ig.print_marginal(System.out, "bronc", true, false);
//        if(1==1) return;
        String csvFile = argv[2];
        int n = Integer.parseInt(argv[3]);
        BufferedReader br = null;
        PrintStream ps = new PrintStream(new PrintStream("vals_" + argv[4]));
        PrintStream ps1 = new PrintStream(new PrintStream("vals_no_" + argv[4]));
        //PrintStream ps2 = new PrintStream(new PrintStream("vals_bic_" + argv[5]));

        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] val = line.split(cvsSplitBy);
                ArrayList<Integer> test = new ArrayList<>();
                for (int i = 0; i < n; i++) { //to generate from 0-10 inclusive. 
                    //For 0-9 inclusive, remove the = on the <=
                    test.add(i);
                }
                Collections.shuffle(test);
                java.util.List<Integer> test1 = test.subList(0, Integer.parseInt(argv[4]));
                System.out.println(test1);

                for (int i = 0; i < n; i++) {
                    if (test1.contains(i)) {
                        System.out.println("v" + Integer.toString(i));
                        if (Integer.parseInt(val[i]) == 1) {
                            ig.setObservations("v" + Integer.toString(i), "yes");
                            ig1.setObservations("v" + Integer.toString(i), "yes");
                            //ig2.setObservations("v" + Integer.toString(i), "yes");

                        } else {
                            ig.setObservations("v" + Integer.toString(i), "no");
                            ig1.setObservations("v" + Integer.toString(i), "no");
                            //ig2.setObservations("v" + Integer.toString(i), "no");

                        }

                    }

                    //ig2.clearAllObservations("v" + Integer.toString(test));
                }

                for (Integer i : test) {

                    if (!test1.contains(i)) {
                        System.out.println(i);

                        ig1.print_marginal(ps, "v" + Integer.toString(i), true, false);
                        ig.print_marginal(ps1, "v" + Integer.toString(i), true, false);
                        //ig2.print_marginal(ps2, "v" + Integer.toString(test), true, false);

                    }
                }
                for (int i = 0; i < n; i++) {
                    if (test1.contains(i)) {
                        ig.clearAllObservations("v" + Integer.toString(i));
                        ig1.clearAllObservations("v" + Integer.toString(i));
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//    JavaBayes jb = new JavaBayes();
//    jb.construct(false);
//    if (argv.length > 0) {
//        String filename = argv[0];
//        System.out.println(filename);
//        jb.open(filename);
//    }
        }

    }

    /**
     * Init method for JavaBayes operating as an applet.
     */
    public void init() {
        construct(true);
    }

    /**
     * Stop method for JavaBayes operating as an applet.
     */
    public void stop() {
        quit();
    }

    /*
   * Do all the initializations for a JavaBayes object. 
     */
    private void construct(boolean ia) {
        is_applet = ia;

        editorFrame = new EditorFrame(this, "JavaBayes Editor");
        editorFrame.show();
        consoleFrame = new JavaBayesConsoleFrame(this, "JavaBayes Console");
        consoleFrame.show();

        JavaBayesHelpMessages.insert(this);
        JavaBayesHelpMessages.show(JavaBayesHelpMessages.about_message);
        JavaBayesHelpMessages.show(JavaBayesHelpMessages.start_message);
    }

    /**
     * Open a file and read the network in it.
     */
    public boolean open(String filename) {
        return (editorFrame.open(filename));
    }

    /**
     * Open a URL and read the network in it.
     */
    public boolean open_url(String filename) {
        return (editorFrame.open_url(filename));
    }

    /**
     * Clear the network.
     */
    public void clear() {
        editorFrame.clear();
        set_current_save_filename(null);
    }

    /**
     * Save the network.
     */
    public boolean save() {
        return (editorFrame.save());
    }

    /**
     * Save the network.
     */
    public boolean save(String filename) {
        return (editorFrame.save(filename));
    }

    /**
     * Interact with menu options: whether to show BucketTree.
     */
    public void what_to_show_bucket_tree_action(boolean what_to_show_bucket_tree) {
        editorFrame.what_to_show_bucket_tree_action(what_to_show_bucket_tree);
    }

    /**
     * Interact with menu options: whether to show bayesian networks. *
     */
    public void what_to_show_bayesian_network_action(boolean what_to_show_bayesian_network) {
        editorFrame.what_to_show_bayesian_network_action(what_to_show_bayesian_network);
    }

    /**
     * Inferences produce expectations.
     */
    public void posterior_expectation_action() {
        editorFrame.posterior_expectation_action();
    }

    /**
     * Inferences produce posterior marginals.
     */
    public void posterior_marginal_action() {
        editorFrame.posterior_marginal_action();
    }

    /**
     * Estimate explanation variables.
     */
    public void estimate_explanation_variables_action() {
        editorFrame.estimate_explanation_variables_action();
    }

    /**
     * Produce the estimates for the best configuration.
     */
    public void estimate_best_configuration_action() {
        editorFrame.estimate_best_configuration_action();
    }

    /**
     * Produce sensitivity analysis.
     */
    public void sensitivity_analysis_action() {
        editorFrame.sensitivity_analysis_action();
    }

    /**
     * Use bif format for saving.
     */
    public void bif_format_action() {
        editorFrame.set_save_format(EditorFrame.BIF_FORMAT);
    }

    /**
     * Use xml format for saving.
     */
    public void xml_format_action() {
        editorFrame.set_save_format(EditorFrame.XML_FORMAT);
    }

    /**
     * Use bugs format for saving.
     */
    public void bugs_format_action() {
        editorFrame.set_save_format(EditorFrame.BUGS_FORMAT);
    }

    /**
     * Quit gracefully.
     */
    public void quit() {
        if (is_applet) {
            editorFrame.hide();
            editorFrame.dispose();
            consoleFrame.hide();
            consoleFrame.dispose();
        } else {
            System.exit(0);
        }
    }

    /**
     * Put text in the consoleFrame.
     */
    public void appendText(String s) {
        consoleFrame.appendText(s);
    }

    /**
     * Get the current filename for saving.
     */
    public String get_current_save_filename() {
        return (editorFrame.get_current_save_filename());
    }

    /**
     * Set the current filename for saving.
     */
    public void set_current_save_filename(String filename) {
        editorFrame.set_current_save_filename(filename);
    }

    /**
     * Set the inference algorithm as variable elimination.
     */
    public void set_algorithm_variable_elimination() {
        editorFrame.set_algorithm(EditorFrame.ALGORITHM_VARIABLE_ELIMINATION);
    }

    /**
     * Set the inference algorithm as bucket tree.
     */
    public void set_algorithm_bucket_tree() {
        editorFrame.set_algorithm(EditorFrame.ALGORITHM_BUCKET_TREE);
    }
}
