/**
 * @since 2024-W38-6 18.39.52.92 -0400
 * @author peter
 */
package com.peter_burbery.postfix_stack.postfix_stack;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;


/**
 * 
 */
public class StackDemonstration {

    private MUStack<Integer> stack;

    /* Constructor initializes the stack */
    public StackDemonstration() {
        this.stack = new MUStack<>();
    }

    /* Overloaded method to demonstrate the stack behavior */
    public void demonstrate(String format, int n, RandomIntegerObject randomIntegerObject) {
        demonstrateStack(format, n, randomIntegerObject);
    }

    /* Main demonstration logic */
    private void demonstrateStack(String format, int n, RandomIntegerObject randomIntegerObject) {
        System.out.println("Demonstrating an empty stack:");
        printStack(format);

        System.out.println("\nAdding 1 element to the stack:");
        stack.push(randomIntegerObject.generate());
        printStack(format);

        System.out.println("\nPopping 1 element from the stack:");
        stack.pop();
        printStack(format);

        // Use Apache Commons StringSubstitutor for interpolation
        String template = "\nAdding ${numElements} random elements to the stack:";
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("numElements", String.valueOf(n));
        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        System.out.println(sub.replace(template));

        for (int i = 0; i < n; i++) {
            stack.push(randomIntegerObject.generate());
        }
        printStack(format);

        System.out.println("\nPeek at the top element:");
        valuesMap.put("topElement", String.valueOf(stack.peek()));
        String peekTemplate = "Top element: ${topElement}";
        System.out.println(sub.replace(peekTemplate));

        System.out.println("Stack after peek (should be unchanged):");
        printStack(format);

        System.out.println("\nChecking if stack is empty (should be false):");
        valuesMap.put("isEmpty", String.valueOf(stack.isEmpty()));
        String emptyTemplate = "Is stack empty? ${isEmpty}";
        System.out.println(sub.replace(emptyTemplate));

        System.out.println("\nPopping all elements from the stack:");
        while (!stack.isEmpty()) {
            stack.pop();
        }
        printStack(format);

        System.out.println("\nChecking if stack is empty (should be true):");
        valuesMap.put("isEmpty", String.valueOf(stack.isEmpty()));
        System.out.println(sub.replace(emptyTemplate));

        System.out.println("\nHandling error: trying to pop from an empty stack:");
        String errorTemplate = "Error: ${errorMessage}";

        try {
            stack.pop();
        } catch (IllegalStateException e) {
            valuesMap.put("errorMessage", e.getMessage());
            System.out.println(sub.replace(errorTemplate));
        }

        System.out.println("\nHandling error: trying to peek from an empty stack:");
        try {
            stack.peek();
        } catch (IllegalStateException e) {
            valuesMap.put("errorMessage", e.getMessage());
            System.out.println(sub.replace(errorTemplate));
        }


        String addElementsTemplate = "\nAdding ${numElements} elements back into the stack:";
        System.out.println(sub.replace(addElementsTemplate));

        for (int i = 0; i < n; i++) {
            stack.push(randomIntegerObject.generate());
        }
        printStack(format);

        System.out.println("\nChecking the size of the stack:");
        valuesMap.put("stackSize", String.valueOf(stack.size()));
        String sizeTemplate = "Stack size: ${stackSize}";
        System.out.println(sub.replace(sizeTemplate));
    }

    /* Helper method to print the stack in the specified format */
    private void printStack(String format) {
        if ("vertical".equals(format)) {
            System.out.println(stack.toString());
        } else if ("horizontal".equals(format)) {
            System.out.println(stack.toStringHorizontal());
        } else {
            System.out.println("Invalid format specified. Use 'vertical' or 'horizontal'.");
        }
    }
}