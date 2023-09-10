$( document ).ready(function() {

    var sse = $.SSE('/alerts', {
        onMessage: function(e){
            // console.log(e);
            let alertArray = e.data.split("#");
            $('#alert-messages tr:last').after('<tr><td>'+alertArray[0]+'</td><td>'+alertArray[1]+'</td><td>'+alertArray[2]+'</td></tr>');
        },
        onError: function(e){
            sse.stop();
            console.log("Could not connect..Stopping SSE");
        },
        onEnd: function(e){
            console.log("End");
        }
    });
    sse.start();

});