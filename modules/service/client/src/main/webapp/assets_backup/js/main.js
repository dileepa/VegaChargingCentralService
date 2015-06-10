(function(){

			// Menu settings
			$('#menuToggle, .menu-close').on('click', function(){
				$('#menuToggle').toggleClass('active');
				$('body').toggleClass('body-push-toleft');
				$('#theMenu').toggleClass('menu-open');
			});

			$.get("ChgCustomerSignUp"
			).success(function(data){
//
                $("#regModal .modal-body").html(data);
                           submitRegistration();
                 });
                 })
(jQuery);


function submitRegistration() {

        $('#registerButton').click(function(e)
                {

                 if($('input[id=passwordSighUp]').val() != $('input[name=verifyPassword]').val())
                 {
                       alert("Error: Please check that you've entered and confirmed your password!");
                       $('input[id=passwordSighUp]').focus();
                       return false;
                       }

                 e.preventDefault();
//
                 var retData = {title: $( "#title" ).val(),
                 firstName: $('input[name=firstName]').val(),
                 lastName:$('input[name=lastName]').val(),
                 address:$('textarea[name=address]').val(),
                 country:$('input[name=country]').val(),
                 email:$('input[name=email]').val(),
                 userName:$('input[id=userNameSighUp]').val(),
                 password:$('input[id=passwordSighUp]').val(),
                 verifyPassword:$('input[name=verifyPassword]').val(),
                 telephone:$('input[name=telephone]').val(),
                 mobileNo:$('input[name=mobileNo]').val(),
                 gender:$( "#gender" ).val(),
                 organizationName:$('input[name=organizationName]').val(),
                 userId:$('input[name=userId]').val(),
                 }
                 $.ajax({
                   type: "POST",
                   url: "saveNewChargeCustomer",
                   data: retData,
                   success: function(data){
                   $("#regModal .modal-body").html(data);
                   submitRegistration();
                   }
                   });
                 });

}


