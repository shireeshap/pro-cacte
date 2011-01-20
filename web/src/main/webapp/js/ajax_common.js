function getStandardParamForAjax() {
    var date = new Date();
    var param = "subview=subview&randNum=" + date.getTime();
    return param;
    
}