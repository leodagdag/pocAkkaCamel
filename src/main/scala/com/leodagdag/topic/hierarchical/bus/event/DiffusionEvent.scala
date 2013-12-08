package com.leodagdag.topic.hierarchical.bus.event

import com.leodagdag.topic.hierarchical.data.Trade

case class DiffusionEvent(channel : String, message: Trade) {}

