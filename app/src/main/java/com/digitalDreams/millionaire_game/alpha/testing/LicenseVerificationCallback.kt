package com.digitalDreams.millionaire_game.alpha.testing

import com.amazon.device.drm.LicensingListener
import com.amazon.device.drm.model.LicenseResponse

// LicenseVerificationCallback function for verifying license
class LicenseVerificationCallback:LicensingListener {
    override fun onLicenseCommandResponse(p0: LicenseResponse?) {
        //val status = p0?.requestStatus

    }
}