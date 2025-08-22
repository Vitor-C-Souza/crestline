package br.com.grooworks.crestline.domain.dto;

import br.com.grooworks.crestline.domain.model.Listing;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Base64;

public record ListingDTO(
        String id,

        @NotBlank(message = "Street Address is required")
        @Size(max = 255, message = "Street Address cannot exceed 255 characters")
        String streetAdress,

        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City cannot exceed 100 characters")
        String city,

        @NotBlank(message = "State is required")
        @Pattern(regexp = "^[A-Z]{2}$", message = "State must be a 2-letter US code (e.g. CA, NY, TX)")
        String state,

        @NotBlank(message = "ZIP Code is required")
        @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "ZIP Code must be in format 12345 or 12345-6789")
        String zipCode,

        @Size(max = 100, message = "County cannot exceed 100 characters")
        String county,


        // Available Utilities
        boolean electricity,
        boolean water,
        boolean sewer,
        boolean naturalGas,
        boolean highSpeedInternet,
        boolean phoneService,

        // Land Features
        boolean pavedRoadAccess,
        boolean gatedCommunity,
        boolean mountainViews,
        boolean waterViews,
        boolean wooded,
        boolean openPasture,
        boolean creekStream,
        boolean pond,
        boolean wellOnProperty,
        boolean septicSystem,
        boolean fenced,
        boolean barnOutbuildings,
        boolean mineralRights,
        boolean huntingRights,

        // Description
        @Size(max = 2000, message = "Description cannot exceed 2000 characters")
        String propertyDescription,

        // Financial Information
        @DecimalMin(value = "0.0", inclusive = false, message = "Listing Price must be greater than 0")
        @Digits(integer = 12, fraction = 2, message = "Listing Price must be a valid monetary value (max 2 decimals)")
        BigDecimal listingPrice,

        @DecimalMin(value = "0.0", inclusive = false, message = "Price per acre must be greater than 0")
        @Digits(integer = 12, fraction = 2, message = "Price per acre must be a valid monetary value (max 2 decimals)")
        BigDecimal pricePerAcre,

        @DecimalMin(value = "0.0", inclusive = false, message = "Down payment must be greater than 0")
        @Digits(integer = 12, fraction = 2, message = "Down payment must be a valid monetary value (max 2 decimals)")
        BigDecimal downPayment,

        @DecimalMin(value = "0.0", inclusive = false, message = "Monthly payment must be greater than 0")
        @Digits(integer = 12, fraction = 2, message = "Monthly payment must be a valid monetary value (max 2 decimals)")
        BigDecimal monthlyPayment,

        @Size(max = 255, message = "Payment terms cannot exceed 255 characters")
        String paymentTerms,

        @NotNull(message = "Profile picture cannot be null")
        String profileProperties
) {
    public ListingDTO(Listing list) {
        this(
                list.getId(),
                list.getStreetAdress(),
                list.getCity(),
                list.getState(),
                list.getZipCode(),
                list.getCounty(),

                // Available Utilities
                list.isElectricity(),
                list.isWater(),
                list.isSewer(),
                list.isNaturalGas(),
                list.isHighSpeedInternet(),
                list.isPhoneService(),

                // Land Features
                list.isPavedRoadAccess(),
                list.isGatedCommunity(),
                list.isMountainViews(),
                list.isWaterViews(),
                list.isWooded(),
                list.isOpenPasture(),
                list.isCreekStream(),
                list.isPond(),
                list.isWellOnProperty(),
                list.isSepticSystem(),
                list.isFenced(),
                list.isBarnOutbuildings(),
                list.isMineralRights(),
                list.isHuntingRights(),

                // Description
                list.getPropertyDescription(),

                // Financial Information
                list.getListingPrice(),
                list.getPricePerAcre(),
                list.getDownPayment(),
                list.getMonthlyPayment(),
                list.getPaymentTerms(),

                // Profile (Base64 encode)
                list.getProfileProperties() != null
                        ? Base64.getEncoder().encodeToString(list.getProfileProperties())
                        : null
        );
    }
}
