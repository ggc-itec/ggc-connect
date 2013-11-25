import java.lang.String;  //import specific packages, not *'s

/**
 * This file is both an example and explanation of adopted coding standards.
 * These standards are mandatory for code check-ins on github and will be
 * rejected if they are not followed.  This may seem harsh but coding standards
 * ensure everyone is on the same page.
 *
 * Code comments should accompany any nonobvious methods describing WHY the method
 * is doing what it does.  If you need to describe WHAT it is doing, your code is
 * wrong.
 *
 * Methods should be no longer than 10-15 lines.  You should be able to describe the method
 * without using the word "and".  Any longer indicates a need for refactoring or
 * poor design descisions.
 *
 * file layout should go as follows:
 *
 * 1. imports
 * 2. class declaration
 * 3. private final/static vars  no public vars
 * 4. private mutable ivars  no public vars
 * 5. default constructor
 * 6. constructor overloads
 * 7. static methods
 * 8. instance methods
 * 9. getters/setters
 * 10. OPTIONAL main methods for testing
 */

/**
 * Java style block comments are used for classes and methods
 */
class CodingStandards  //classes/interfaces/enums begin with uppercase letters and cammel case
{  //braces go on the next line, aligned vertically

    private final int USEFUL_CONST_NAME = 1;  //constants are in all caps and snake case

    private static String extremelyDescriptiveName = "bob";  //camel case

    private int usefulName;  //ivars are ALWAYS private and cammel case
    //names should be descriptive, prefer verbosity to terseness.

    public CodingStandards()  //default constructor
    {

    }

    public CodingStandards(int descriptiveName, int overlVariable)  //constructor overload
    {

    }

    public static String fetchUsefullConstName() //static methods go after constructors
    {
        return extremelyDescriptiveName;
    }

    /**
     * Nonobvious methods should have appropriate java style documentation to
     * accompany them.
     */
    public void doSomethingNonObvious()
    {

    }

    public static void main(String[] args) //main methods may be used for quick testing if necessary.
    {
        assert 1 : 1;  // primitive testing
    }

    // Getters / Setters     --------------------------------------------
    int getUSEFUL_CONST_NAME()
    {
        return USEFUL_CONST_NAME;
    }

    static String getExtremelyDescriptiveName()
    {
        return extremelyDescriptiveName;
    }

    static void setExtremelyDescriptiveName(String extremelyDescriptiveName)
    {
        CodingStandards.extremelyDescriptiveName = extremelyDescriptiveName;
    }

    int getUsefulName()
    {
        return usefulName;
    }

    void setUsefulName(int usefulName)
    {
        this.usefulName = usefulName;
    }
}
