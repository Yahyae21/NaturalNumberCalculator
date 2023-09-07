import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;

/**
 * Controller class.
 *
 * @author Put your name here
 */
public final class NNCalcController1 implements NNCalcController {

    /**
     * Model object.
     */
    private final NNCalcModel model;

    /**
     * View object.
     */
    private final NNCalcView view;

    /**
     * Useful constants.
     */
    private static final NaturalNumber TWO = new NaturalNumber2(2),
            INT_LIMIT = new NaturalNumber2(Integer.MAX_VALUE);

    /**
     * Updates this.view to display this.model, and to allow only operations
     * that are legal given this.model.
     *
     * @param model
     *            the model
     * @param view
     *            the view
     * @ensures [view has been updated to be consistent with model]
     */
    private static void updateViewToMatchModel(NNCalcModel model,
            NNCalcView view) {

        /*
         * Get model info
         */
        NaturalNumber topNum = new NaturalNumber2();
        NaturalNumber bottomNum = new NaturalNumber2();
        topNum = model.top();
        bottomNum = model.bottom();
        boolean subtract = false;
        boolean divide = false;
        boolean power = false;
        boolean root = false;

        if (bottomNum.compareTo(topNum) <= 0) {
            subtract = true;

        }

        if (!bottomNum.isZero()) {
            divide = true;
        }

        if (bottomNum.compareTo(INT_LIMIT) <= 0) {
            power = true;

        }
        if (bottomNum.compareTo(TWO) >= 0
                && bottomNum.compareTo(INT_LIMIT) <= 0) {
            root = true;
        }
        /*
         * Update view to reflect changes in model
         */
        view.updateTopDisplay(topNum);
        view.updateBottomDisplay(bottomNum);
        view.updateSubtractAllowed(subtract);
        view.updateDivideAllowed(divide);
        view.updatePowerAllowed(power);
        view.updateRootAllowed(root);

    }

    /**
     * Constructor.
     *
     * @param model
     *            model to connect to
     * @param view
     *            view to connect to
     */
    public NNCalcController1(NNCalcModel model, NNCalcView view) {
        this.model = model;
        this.view = view;
        updateViewToMatchModel(model, view);
    }

    @Override
    public void processClearEvent() {
        /*
         * Get alias to bottom from model
         */
        NaturalNumber bottom = this.model.bottom();
        /*
         * Update model in response to this event
         */
        bottom.clear();
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processSwapEvent() {
        /*
         * Get aliases to top and bottom from model
         */
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        /*
         * Update model in response to this event
         */
        NaturalNumber temp = top.newInstance();
        temp.transferFrom(top);
        top.transferFrom(bottom);
        bottom.transferFrom(temp);
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    /**
     * Processes event to enter bottom operand to top.
     *
     * @updates this.model.top, this.view
     * @ensures <pre>
     * this.model.top = this.model.bottom  and
     * [this.view has been updated to match this.model]
     * </pre>
     */

    @Override
    public void processEnterEvent() {
        /*
         * Get aliases to top and bottom from model
         */
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();

        //update model in response to this event
        top.copyFrom(bottom);

        updateViewToMatchModel(this.model, this.view);

    }

    /**
     * Processes event to do an add operation.
     *
     * @updates this.model, this.view
     * @ensures <pre>
     * this.model.top = 0  and
     * this.model.bottom = #this.model.top + #this.model.bottom  and
     * [this.view has been updated to match this.model]
     * </pre>
     */

    @Override
    public void processAddEvent() {

        /*
         * Get aliases to top and bottom from model
         */
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();

        top.add(bottom);
        bottom.transferFrom(top);

        updateViewToMatchModel(this.model, this.view);

    }

    /**
     * Processes event to do a subtract operation.
     *
     * @updates this.model, this.view
     * @requires this.model.bottom <= this.model.top
     * @ensures <pre>
     * this.model.top = 0  and
     * this.model.bottom = #this.model.top - #this.model.bottom  and
     * [this.view has been updated to match this.model]
     * </pre>
     */

    @Override
    public void processSubtractEvent() {

        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();

        top.subtract(bottom);
        bottom.transferFrom(top);

        updateViewToMatchModel(this.model, this.view);

    }

    /**
     * Processes event to do a multiply operation.
     *
     * @updates this.model, this.view
     * @ensures <pre>
     * this.mode.top = 0  and
     * this.model.bottom = #this.model.top * #this.model.bottom  and
     * [this.view has been updated to match this.model]
     * </pre>
     */

    @Override
    public void processMultiplyEvent() {

        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        top.multiply(bottom);
        bottom.transferFrom(top);

        updateViewToMatchModel(this.model, this.view);

    }

    /**
     * Processes event to do a divide operation.
     *
     * @updates this.model, this.view
     * @requires this.model.bottom > 0
     * @ensures <pre>
     * #this.model.top =
     *   this.model.bottom * #this.model.bottom + this.model.top  and
     * 0 <= this.model.top < #this.model.bottom  and
     * [this.view has been updated to match this.model]
     * </pre>
     */

    @Override
    public void processDivideEvent() {

        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();

        NaturalNumber remainder = top.divide(bottom);

        bottom.transferFrom(top);
        top.transferFrom(remainder);

        updateViewToMatchModel(this.model, this.view);

    }

    /**
     * Processes event to do a power operation.
     *
     * @updates this.model, this.view
     * @requires this.model.bottom <= INT_LIMIT
     * @ensures <pre>
     * this.model.top = 0  and
     * this.model.bottom = #this.model.top ^ (#this.model.bottom)  and
     * [this.view has been updated to match this.model]
     * </pre>
     */
    @Override
    public void processPowerEvent() {
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();

        top.power(bottom.toInt());
        bottom.transferFrom(top);
        updateViewToMatchModel(this.model, this.view);

    }

    /**
     * Processes event to do a root operation.
     *
     * @updates this.model, this.view
     * @requires 2 <= this.model.bottom <= INT_LIMIT
     * @ensures <pre>
     * this.model.top = 0  and
     * this.model.bottom =
     *   [the floor of the #this.model.bottom root of #this.model.top]  and
     * [this.view has been updated to match this.model]
     * </pre>
     */

    @Override
    public void processRootEvent() {

        /*
         * Get aliases to top and bottom from model
         */
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        top.root(bottom.toInt());
        bottom.transferFrom(top);
        updateViewToMatchModel(this.model, this.view);

    }

    /**
     * Processes event to add a new (low-order) digit to the bottom operand.
     *
     * @param digit
     *            the low-order digit to be added
     *
     * @updates this.model.bottom, this.view
     * @requires 0 <= digit < 10
     * @ensures <pre>
     * this.model.bottom = #this.model.bottom * 10 + digit  and
     * [this.view has been updated to match this.model]
     * </pre>
     */

    @Override
    public void processAddNewDigitEvent(int digit) {

        this.model.bottom().multiplyBy10(digit);
        updateViewToMatchModel(this.model, this.view);

    }

}
