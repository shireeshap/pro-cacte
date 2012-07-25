/**
 * Created by IntelliJ IDEA.
 * User: Reshma Koganti
 * Date: Jan 25, 2011
 * Time: 2:57:28 PM
 * To change this template use File | Settings | File Templates.
 */

function showOrHideErrorField(errorValue, id) {
    if (errorValue) {
        jQuery(id).show();
    }
    else {
        jQuery(id).hide();
    }

}
