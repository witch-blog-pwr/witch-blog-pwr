## Password policy

On backend there is a password validation with 
the following password policy:
 * at least one digit
 * at least one lowercase letter
 * at least one uppercase letter
 * at least one special character
 * no whitespaces
 * length between 8 and 64 characters

If password is incompatible with this policy,
backend will return HttpStatus.BAD_REQUEST.
