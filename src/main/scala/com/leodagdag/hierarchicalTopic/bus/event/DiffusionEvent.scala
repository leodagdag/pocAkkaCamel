package com.leodagdag.hierarchicalTopic.event

import com.leodagdag.hierarchicalTopic.data.Trade

/**
 * Created by leo on 07/12/13.
 */
case class DiffusionEvent(channel : String, message: Trade) {}

