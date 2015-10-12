(function ($){
    $.fn.extend({
        hoverShow:function(type,css){
            var newDiv = null;
            $(this).hover(
                function(){
                    var baseCss = $.extend({zIndex:101,position:'absolute'},getMouseXY(event));
                    newDiv = $('<div/>').appendTo($('body'));
                    newDiv.css($.extend(css,baseCss)).html($(this).attr(type));
                },function(){
                    if(newDiv){
                        newDiv.remove();
                    }
                }
            );
            function getMouseXY(event){
            	var topPoor = 20;
            	var leftPoor = 5;
            	if(event.pageX || event.pageY){
                    return {left:event.pageX+leftPoor, top:event.pageY+ topPoor};}
                return {
                    left:leftPoor+event.clientX + document.body.scrollLeft - document.body.clientLeft,
                    top:topPoor+event.clientY + document.body.scrollTop  - document.body.clientTop
                };
                
                
            }
        }
    });
})(jQuery);