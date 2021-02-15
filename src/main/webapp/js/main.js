

$(document).on('change keyup', '.required', function(e){
    let Disabled = true;
    $(".required").each(function() {
        let value = this.value
        if ((value)&&(value.trim() !=''))
        {
            Disabled = false
        }else{
            Disabled = true
            return false
        }
    });

    if(Disabled){
        $('.toggle-disabled').prop("disabled", true);
    }else{
        $('.toggle-disabled').prop("disabled", false);
    }
});