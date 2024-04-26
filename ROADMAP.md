# Roadmap

### Implementation notes

I've used a precision of 2 for monetary values and 6 for interest as this allowed me to recreate values in the exercise examples.
My results are off by a few pennies, possibly the examples calculate money with more precision?
In the example there is some incorrect looking entries for example:

- period 8 balance is 8515.41
- period 8 princile is 1681.93
- 8515.41 - 1681.93 = 6833.48 but example period 8 balance is 6833.49
- My period 8 balance is 6833.48. Hopefully this is ok

To make sure penny differences are accounted for the last installment always increases/decreases the last payment to bring the balance to zero or the balloon payment

### Code Improvements

- AmortisationApiTest is not as comprehensive as Id like and needs a refactor, unfortunately I am out of time.
- Error handling by ControllerAdvice could be more comprehensive. Again I am out of time
- AmortisationService.createAmortisation is a bit awkward but I prefer the db to handle entry Ids

### Future Improvements

- Currently no way of setting currency, just assumes a 2dp currency is being used. Should add a way of setting currency on request and a conversion service.
- Some variables such as interest precision should be set in a config file or by container environment
- Some config entries have plaintext passwords. These should be supplied in a secure manner.
- Make payment period configurable either through config or allow user to specify in request json
- Db Entries should be made immutable for auditing purposes