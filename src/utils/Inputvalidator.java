package utils;

import javax.swing.*;
import javax.swing.text.*;

public class Inputvalidator {

    // Call this method to restrict any JTextField to digits only
    public static void makeNumericOnly(JTextField textField) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
    }

    // Inner class that defines the numeric filter
    private static class NumericDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string != null && string.matches("\\d+")||string.isEmpty()) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text != null) {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                StringBuilder sb = new StringBuilder(currentText);
                sb.replace(offset, offset + length, text);

                // Validate the final result (not just the inserted text)
                if (sb.toString().matches("\\d*")) { // allow empty string
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        }
    }
}
